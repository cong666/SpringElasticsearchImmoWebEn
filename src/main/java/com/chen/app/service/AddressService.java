package com.chen.app.service;

import com.chen.app.jpa.model.Subway;
import com.chen.app.jpa.model.SubwayStation;
import com.chen.app.jpa.model.SupportAddress;
import com.chen.app.jpa.repository.SubwayRepository;
import com.chen.app.jpa.repository.SubwayStationRepository;
import com.chen.app.jpa.repository.SupportAddressRepository;
import com.chen.web.dto.SubwayDTO;
import com.chen.web.dto.SubwayStationDTO;
import com.chen.web.dto.SupportAddressDTO;
import com.chen.web.util.ModelMapper;
import com.chen.web.util.ServiceMultipleResult;
import com.chen.web.util.ServiceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by: ccong
 * Date: 18/8/26 下午8:13
 */
@Service
public class AddressService {
    @Autowired
    private SupportAddressRepository supportAddressRepository;
    @Autowired
    private SubwayRepository subwayRepository;
    @Autowired
    private SubwayStationRepository subwayStationRepository;

    public ServiceMultipleResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> supportAddressList = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());
        List<SupportAddressDTO> supportAddressDTOList = new ArrayList<>();
        for (SupportAddress supportAddress : supportAddressList) {
            SupportAddressDTO dto = ModelMapper.toSupportAddressDTO(supportAddress);
            supportAddressDTOList.add(dto);
        }
        return new ServiceMultipleResult(supportAddressDTOList.size(),supportAddressDTOList);
    }

    public ServiceMultipleResult<SupportAddressDTO> findAllRegionByCityName(String cityEnName) {
        if(cityEnName==null){
            return new ServiceMultipleResult<>(0, null);
        }
        List<SupportAddressDTO> result = new ArrayList<>();
        List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION
                .getValue(), cityEnName);
        regions.forEach(region -> {
            result.add(ModelMapper.toSupportAddressDTO(region));
        });
        return new ServiceMultipleResult<>(regions.size(), result);
    }

    public List<SubwayDTO> findAllSubwayByCity(String cityEnName) {
        List<SubwayDTO> result = new ArrayList<>();
        List<Subway> subways = subwayRepository.findAllByCityEnName(cityEnName);
        subways.forEach(subway -> result.add(ModelMapper.toSubwayDTO(subway)));
        return result;
    }

    public List<SubwayStationDTO> findAllStationBySubway(Long subwayId) {
        List<SubwayStationDTO> result = new ArrayList<>();
        List<SubwayStation> stations = subwayStationRepository.findAllBySubwayId(subwayId);
        if (stations.isEmpty()) {
            return result;
        }

        stations.forEach(station -> result.add(ModelMapper.toSubwayStationDTO(station)));
        return result;
    }

    public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();

        SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY
                .getValue());
        SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName, city.getEnName());

        result.put(SupportAddress.Level.CITY, ModelMapper.toSupportAddressDTO(city));
        result.put(SupportAddress.Level.REGION, ModelMapper.toSupportAddressDTO(region));
        return result;
    }

    public ServiceResult<SubwayDTO> findSubway(Long subwayId) {
        if (subwayId == null) {
            return ServiceResult.notFound();
        }
        Subway subway = subwayRepository.getOne(subwayId);
        if (subway == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(ModelMapper.toSubwayDTO(subway));
    }

    public ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId) {
        if (stationId == null) {
            return ServiceResult.notFound();
        }
        SubwayStation station = subwayStationRepository.getOne(stationId);
        if (station == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(ModelMapper.toSubwayStationDTO(station));
    }

    public ServiceResult<SupportAddressDTO> findCity(String cityEnName) {
        if (cityEnName == null) {
            return ServiceResult.notFound();
        }

        SupportAddress supportAddress = supportAddressRepository.findByEnNameAndLevel(cityEnName, SupportAddress.Level.CITY.getValue());
        if (supportAddress == null) {
            return ServiceResult.notFound();
        }

        SupportAddressDTO addressDTO = ModelMapper.toSupportAddressDTO(supportAddress);
        return ServiceResult.of(addressDTO);
    }

    public ServiceMultipleResult<SupportAddressDTO> findAllRegionsByCityName(String cityName) {
        if (cityName == null) {
            return new ServiceMultipleResult<>(0, null);
        }

        List<SupportAddressDTO> result = new ArrayList<>();

        List<SupportAddress> regions = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION
                .getValue(), cityName);
        for (SupportAddress region : regions) {
            result.add(ModelMapper.toSupportAddressDTO(region));
        }
        return new ServiceMultipleResult<>(regions.size(), result);
    }
}
