package com.chen.app.service;

import com.chen.app.base.HouseSort;
import com.chen.app.base.HouseStatus;
import com.chen.app.base.LoginUserUtil;
import com.chen.app.jpa.model.*;
import com.chen.app.jpa.repository.*;
import com.chen.app.search.SearchService;
import com.chen.web.dto.HouseDTO;
import com.chen.web.dto.HouseDetailDTO;
import com.chen.web.dto.HousePictureDTO;
import com.chen.web.form.DataTableSearch;
import com.chen.web.form.HouseForm;
import com.chen.web.form.PhotoForm;
import com.chen.web.form.RentFilter;
import com.chen.web.util.ServiceMultipleResult;
import com.chen.web.util.ServiceResult;
import com.google.common.collect.Maps;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class HouseService {

    private static final Logger logger = LoggerFactory.getLogger(HouseService.class);

    @Autowired
    private Environment environment;

    private String cdnPrefix;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private SubwayStationRepository subwayStationRepository;

    @Autowired
    private HouseDetailRepository houseDetailRepository;

    @Autowired
    private HousePictureRepository housePictureRepository;

    @Autowired
    private HouseTagRepository houseTagRepository;

    @Autowired
    private SearchService searchService;


    @PostConstruct
    public void init() {
        cdnPrefix = this.environment.getRequiredProperty("spring.http.multipart.location");
    }
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        HouseDetail detail = new HouseDetail();
        ServiceResult<HouseDTO> subwayValidtionResult = wrapperDetailInfo(detail, houseForm);
        if (subwayValidtionResult != null) {
            return subwayValidtionResult;
        }

        House house = new House();
        //map the houseform data into house object
        modelMapper.map(houseForm, house);
        Date now = new Date();
        house.setCreateTime(now);
        house.setLastUpdateTime(now);
        house.setAdminId(LoginUserUtil.getLoginUser());
        houseRepository.save(house);
        //TODO HouseDetail
        detail.setHouseId(house.getId());
        detail = houseDetailRepository.save(detail);

        List<HousePicture> pictures = generatePictures(houseForm, house.getId());
        Iterable<HousePicture> housePictures = housePictureRepository.saveAll(pictures);

        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        HouseDetailDTO houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);

        houseDTO.setHouseDetail(houseDetailDTO);

        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        housePictures.forEach(housePicture -> pictureDTOS.add(modelMapper.map(housePicture, HousePictureDTO.class)));
        houseDTO.setPictures(pictureDTOS);
        houseDTO.setCover(this.cdnPrefix+"/" + houseDTO.getCover());

        List<String> tags = houseForm.getTags();
        if (tags != null && !tags.isEmpty()) {
            List<HouseTag> houseTags = new ArrayList<>();
            for (String tag : tags) {
                houseTags.add(new HouseTag(house.getId(), tag));
            }
            houseTagRepository.saveAll(houseTags);
            houseDTO.setTags(tags);
        }

        return new ServiceResult<HouseDTO>(true, null, houseDTO);
    }

    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail houseDetail, HouseForm houseForm) {
        Subway subway = subwayRepository.getOne(houseForm.getSubwayLineId());
        if (subway == null) {
            return new ServiceResult<>(false, "Not valid subway line!");
        }

        SubwayStation subwayStation = subwayStationRepository.getOne(houseForm.getSubwayStationId());
        if (subwayStation == null || subway.getId() != subwayStation.getSubwayId()) {
            return new ServiceResult<>(false, "Not valid subway station!");
        }

        houseDetail.setSubwayLineId(subway.getId());
        houseDetail.setSubwayLineName(subway.getName());

        houseDetail.setSubwayStationId(subwayStation.getId());
        houseDetail.setSubwayStationName(subwayStation.getName());

        houseDetail.setDescription(houseForm.getDescription());
        houseDetail.setDetailAddress(houseForm.getDetailAddress());
        houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
        houseDetail.setRentWay(houseForm.getRentWay());
        houseDetail.setRoundService(houseForm.getRoundService());
        houseDetail.setTraffic(houseForm.getTraffic());
        return null;
    }

    private List<HousePicture> generatePictures(HouseForm form, Long houseId) {
        List<HousePicture> pictures = new ArrayList<>();
        if (form.getPhotos() == null || form.getPhotos().isEmpty()) {
            //FIXME : create the default pictures
            HousePicture picture = new HousePicture();
            picture.setHouseId(houseId);
            picture.setCdnPrefix(cdnPrefix);
            picture.setPath(cdnPrefix+"/house.jpg");
            picture.setWidth(394);
            picture.setHeight(525);
            pictures.add(picture);
            return pictures;
        }

        for (PhotoForm photoForm : form.getPhotos()) {
            HousePicture picture = new HousePicture();
            picture.setHouseId(houseId);
            picture.setCdnPrefix(cdnPrefix);
            picture.setPath(photoForm.getPath());
            picture.setWidth(photoForm.getWidth());
            picture.setHeight(photoForm.getHeight());
            pictures.add(picture);
        }
        return pictures;
    }

    public ServiceMultipleResult<HouseDTO> adminQuery(DataTableSearch search) {
        List<HouseDTO> houseDTOS = new ArrayList<>();

        Sort sort = new Sort(Sort.Direction.fromString(search.getDirection()), search.getOrderBy());
        int page = search.getStart() / search.getLength();
        System.out.println("adminQuery is called, page : "+page+
                ",search info:"+search.getStart()+","
                +search.getLength()+","+search.getStart());
        Pageable pageable =  PageRequest.of(page, search.getLength(), sort);

        Specification<House> specification = (root, query, cb)->{

            javax.persistence.criteria.Predicate predicate = cb.equal(root.get("adminId"), LoginUserUtil.getLoginUser());
            predicate = cb.and(predicate, cb.notEqual(root.get("status"), HouseStatus.DELETED.getValue()));

            if(search.getCity() != null){
                System.out.println("-------getCity:"+search.getCity());
                predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), search.getCity()));
            }
            if(search.getStatus() != null){
                System.out.println("-------getStatus:"+search.getStatus());
                predicate = cb.and(predicate, cb.equal(root.get("status"), search.getStatus()));
            }
            if(search.getCreateTimeMin() != null){
                System.out.println("-------getCreateTimeMin:"+search.getCreateTimeMin());
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime"), search.getCreateTimeMin()));
            }
            if (search.getCreateTimeMax() != null){
                System.out.println("-------getCreateTimeMax:"+search.getCreateTimeMax());
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createTime"), search.getCreateTimeMax()));
            }
            if(search.getTitle() != null){
                System.out.println("-------getTitle:"+search.getTitle());
                predicate = cb.and(predicate, cb.like(root.get("title"), "%"+search.getTitle()+"%"));
            }
            return predicate;
        };

        Page<House> housePage = houseRepository.findAll(specification,pageable);
        Iterator<House> it = housePage.iterator();

        while(it.hasNext()) {
            House house = it.next();
            HouseDTO houseDTO =modelMapper.map(house,HouseDTO.class);
            houseDTOS.add(houseDTO);
        }

        System.out.println("HouseDTOS size : "+houseDTOS.size());
        return new ServiceMultipleResult(houseDTOS.size(), houseDTOS);
    }

    public ServiceResult<HouseDTO> findCompleteOne(Long id) {
        House house = houseRepository.getOne(id);
        if(house == null){
            return ServiceResult.notFound();
        }

        HouseDetail detail = houseDetailRepository.findByHouseId(id);
        List<HousePicture> pictures = housePictureRepository.findAllByHouseId(id);

        HouseDetailDTO detailDTO = modelMapper.map(detail, HouseDetailDTO.class );
        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        pictures.forEach(p -> {
            pictureDTOS.add(modelMapper.map(p, HousePictureDTO.class));
        });

        List<HouseTag> tags = houseTagRepository.findAllByHouseId(id);
        List<String> tagList = new ArrayList<>();
        tags.forEach(tag -> tagList.add(tag.getName()));

        HouseDTO result = modelMapper.map(house, HouseDTO.class);
        result.setHouseDetail(detailDTO);
        result.setPictures(pictureDTOS);
        result.setTags(tagList);

        return ServiceResult.of(result);
    }

    @Transactional
    public ServiceResult update(HouseForm houseForm) {
        House house = this.houseRepository.getOne(houseForm.getId());
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseDetail detail = this.houseDetailRepository.findByHouseId(house.getId());
        if (detail == null) {
            return ServiceResult.notFound();
        }

        ServiceResult wrapperResult = wrapperDetailInfo(detail, houseForm);
        if (wrapperResult != null) {
            return wrapperResult;
        }

        houseDetailRepository.save(detail);

        List<HousePicture> pictures = generatePictures(houseForm, houseForm.getId());
        housePictureRepository.saveAll(pictures);

        if (houseForm.getCover() == null) {
            houseForm.setCover(house.getCover());
        }

        modelMapper.map(houseForm, house);
        house.setLastUpdateTime(new Date());
        houseRepository.save(house);

        if(house.getStatus() == HouseStatus.PASSES.getValue()) {
            searchService.index(house.getId());
        }

        return ServiceResult.success();
    }

    public ServiceResult removePhoto(Long id) {
        HousePicture picture = housePictureRepository.getOne(id);
        if (picture == null) {
            return ServiceResult.notFound();
        }

        return new ServiceResult(false, "Not Support");
    }

    @Transactional
    public ServiceResult updateCover(Long coverId, Long targetId) {
        HousePicture cover = housePictureRepository.getOne(coverId);
        if (cover == null) {
            return ServiceResult.notFound();
        }

        houseRepository.updateCover(targetId, cover.getPath());
        return ServiceResult.success();
    }

    @Transactional
    public ServiceResult removeTag(Long houseId, String tag) {
        House house = houseRepository.getOne(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
        if (houseTag == null) {
            return new ServiceResult(false, "Tag not exist");
        }

        houseTagRepository.deleteById(houseTag.getId());
        return ServiceResult.success();
    }

    @Transactional
    public ServiceResult addTag(Long houseId, String tag) {
        House house = houseRepository.getOne(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
        if (houseTag != null) {
            return new ServiceResult(false, "Tag not exist");
        }

        houseTagRepository.save(new HouseTag(houseId, tag));
        return ServiceResult.success();
    }


    @Transactional
    public ServiceResult updateStatus(Long id, int status) {
        House house = houseRepository.getOne(id);
        if (house == null) {
            return ServiceResult.notFound();
        }

        if (house.getStatus() == status) {
            return new ServiceResult(false, "Status not changed");
        }

        if (house.getStatus() == HouseStatus.RENTED.getValue()) {
            return new ServiceResult(false, "House is already rent");
        }

        if (house.getStatus() == HouseStatus.DELETED.getValue()) {
            return new ServiceResult(false, "House is already deleted");
        }

        houseRepository.updateStatus(id, status);

        if(status == HouseStatus.PASSES.getValue()) {
            searchService.index(house.getId());
        } else {
            searchService.remove(house.getId());
        }

        return ServiceResult.success();
    }

    /**
     * @param houseIds
     * @param idToHouseMap
     */
    private void wrapperHouseList(List<Long> houseIds, Map<Long, HouseDTO> idToHouseMap) {
        List<HouseDetail> details = houseDetailRepository.findAllByHouseIdIn(houseIds);
        details.forEach(houseDetail -> {
            HouseDTO houseDTO = idToHouseMap.get(houseDetail.getHouseId());
            HouseDetailDTO detailDTO = modelMapper.map(houseDetail, HouseDetailDTO.class);
            houseDTO.setHouseDetail(detailDTO);
        });

        List<HouseTag> houseTags = houseTagRepository.findAllByHouseIdIn(houseIds);
        houseTags.forEach(houseTag -> {
            HouseDTO house = idToHouseMap.get(houseTag.getHouseId());
            house.getTags().add(houseTag.getName());
        });
    }

    private List<HouseDTO> wrapperHouseResult(List<Long> houseIds) {
        List<HouseDTO> result = new ArrayList<>();

        Map<Long, HouseDTO> idToHouseMap = new HashMap<>();
        Iterable<House> houses = houseRepository.findAllById(houseIds);
        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(this.cdnPrefix+"/" + house.getCover().substring(21,house.getCover().length()));
            idToHouseMap.put(house.getId(), houseDTO);
        });

        wrapperHouseList(houseIds, idToHouseMap);
        for (Long houseId : houseIds) {
            result.add(idToHouseMap.get(houseId));
        }
        return result;
    }

    public ServiceMultipleResult<HouseDTO> query(RentFilter rentFilter) {
        if (rentFilter.getKeywords() != null && !rentFilter.getKeywords().isEmpty()) {
            ServiceMultipleResult<Long> serviceResult = searchService.query(rentFilter);
            if (serviceResult.getTotal() == 0) {
                return new ServiceMultipleResult<>(0, new ArrayList<>());
            }

            return new ServiceMultipleResult<>(serviceResult.getTotal(), wrapperHouseResult(serviceResult.getResult()));
        }

        return simpleQuery(rentFilter);

    }

    private ServiceMultipleResult<HouseDTO> simpleQuery(RentFilter rentFilter) {
        logger.debug("simpleQuery is called");
        Sort sort = HouseSort.generateSort(rentFilter.getOrderBy(), rentFilter.getOrderDirection());
        int page = rentFilter.getStart() / rentFilter.getSize();

        Pageable pageable = new PageRequest(page, rentFilter.getSize(), sort);

        Specification<House> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("status"), HouseStatus.PASSES.getValue());

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cityEnName"), rentFilter.getCityEnName()));

            if (HouseSort.DISTANCE_TO_SUBWAY_KEY.equals(rentFilter.getOrderBy())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.gt(root.get(HouseSort.DISTANCE_TO_SUBWAY_KEY), -1));
            }
            return predicate;
        };

        Page<House> houses = houseRepository.findAll(specification, pageable);
        List<HouseDTO> houseDTOS = new ArrayList<>();


        List<Long> houseIds = new ArrayList<>();
        Map<Long, HouseDTO> idToHouseMap = Maps.newHashMap();
        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(this.cdnPrefix+"/" + house.getCover().substring(21,house.getCover().length()));
            houseDTOS.add(houseDTO);

            houseIds.add(house.getId());
            idToHouseMap.put(house.getId(), houseDTO);
        });


        wrapperHouseList(houseIds, idToHouseMap);
        return new ServiceMultipleResult<>(houses.getTotalElements(), houseDTOS);
    }

}
