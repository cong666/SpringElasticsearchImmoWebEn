package com.chen.web.controller;

import com.chen.app.base.ApiMessage;
import com.chen.app.base.DataTablesResponse;
import com.chen.app.base.HouseOperation;
import com.chen.app.base.HouseStatus;
import com.chen.app.jpa.model.SupportAddress;
import com.chen.app.service.AddressService;
import com.chen.app.service.HouseService;
import com.chen.web.dto.*;
import com.chen.web.form.DataTableSearch;
import com.chen.web.form.HouseForm;
import com.chen.web.form.PhotoForm;
import com.chen.web.util.ServiceMultipleResult;
import com.chen.web.util.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by: ccong
 * Date: 18/8/25 下午11:36
 */
@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private HouseService houseService;

    @GetMapping("/admin/center")
    public String adminCenterPage() {
        return "admin/center";
    }

    @GetMapping("/admin/welcome")
    public String adminWelcome() {
        return "admin/welcome";
    }

    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "admin/login";
    }

    @GetMapping("/admin/add/house")
    public String adminAddHousePage() {
        return "admin/house-add";
    }

    @GetMapping("admin/house/list")
    public String houseListPage() {
        return "admin/house-list";
    }

    @PostMapping(value = "admin/upload/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ApiMessage uploadPhoto(@RequestParam("file") MultipartFile file) {
        System.out.println("uploadPhoto is called");
        if (file.isEmpty()) {
            return ApiMessage.ofStatus(ApiMessage.Status.NOT_VALID_PARAM);
        }
        String fileName = file.getOriginalFilename();
        File targetFile = new File("/Users/ccong/Documents/J2EEProject/ImoocProject/SpringElasticsearchImmo/SpringElasticsearchImmoWeb/src/main/resources/static/images/upload/"+fileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiMessage.ofStatus(ApiMessage.Status.INTERNAL_SERVER_ERROR);
        }
        PhotoForm pf = new PhotoForm();
        pf.setPath("static/images/upload/"+fileName);
        return ApiMessage.ofSuccess(pf);
    }

    @ResponseBody
    @PostMapping("admin/add/house")
    public ApiMessage addHouse(@Valid @ModelAttribute("form-house-add") HouseForm houseForm,
                                   BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ApiMessage(HttpStatus.BAD_REQUEST.value(),
                    bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
        }
        if(houseForm.getPhotos() == null || houseForm.getCover() == null){
            return ApiMessage.ofMessage(HttpStatus.BAD_REQUEST.value(), "Must upload the image");
        }

        Map<SupportAddress.Level, SupportAddressDTO> map = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());
        if(map.keySet().size() != 2){
            return ApiMessage.ofStatus(ApiMessage.Status.NOT_VALID_PARAM);
        }
        ServiceResult<HouseDTO> result = houseService.save(houseForm);
        if(result.isSuccess()){
            return ApiMessage.ofSuccess(result.getResult());
        }else{
            return ApiMessage.ofStatus(ApiMessage.Status.NOT_VALID_PARAM);
        }

    }

    @ResponseBody
    @PostMapping("admin/houses")
    public ResponseEntity houses(@ModelAttribute DataTableSearch search){
        logger.debug("admin/houses: DatableTableSearch: "+search);
        //search is from house-list.js : postData
        ServiceMultipleResult<HouseDTO> result = houseService.adminQuery(search);
        DataTablesResponse dataTablesResponse = new DataTablesResponse();
        dataTablesResponse.setRecordsFiltered(result.getTotal());
        dataTablesResponse.setRecordsTotal(result.getTotal());
        dataTablesResponse.setDraw(search.getDraw());
        dataTablesResponse.setData(result.getResult());
        return new ResponseEntity(dataTablesResponse, HttpStatus.OK);
    }

    @GetMapping("admin/house/edit")
    public String houseEditPage(@RequestParam(value = "id") Long id, Model model){
        if(id ==null || id < 1){
            return "404";
        }

        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(id);
        if(!serviceResult.isSuccess()){
            return "404";
        }

        HouseDTO result = serviceResult.getResult();
        model.addAttribute("house", result);

        Map<SupportAddress.Level, SupportAddressDTO> addressDTOMap = addressService.findCityAndRegion(result.getCityEnName(), result.getRegionEnName());
        model.addAttribute("city", addressDTOMap.get(SupportAddress.Level.CITY));
        model.addAttribute("region", addressDTOMap.get(SupportAddress.Level.REGION));

        HouseDetailDTO detailDTO = result.getHouseDetail();
        ServiceResult<SubwayDTO> subwayDTOServiceResult = addressService.findSubway(detailDTO.getSubwayLineId());
        if(subwayDTOServiceResult.isSuccess()){
            model.addAttribute("subway", subwayDTOServiceResult.getResult());
        }
        ServiceResult<SubwayStationDTO> subwayStationDTOServiceResult = addressService.findSubwayStation(detailDTO.getSubwayStationId());
        if(subwayStationDTOServiceResult.isSuccess()){
            model.addAttribute("station", subwayStationDTOServiceResult.getResult());
        }
        return "admin/house-edit";
    }

    /**
     * 编辑接口
     */
    @PostMapping("admin/house/edit")
    @ResponseBody
    public ApiMessage saveHouse(@Valid @ModelAttribute("form-house-edit") HouseForm houseForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ApiMessage.ofMessage(HttpStatus.BAD_REQUEST.value(),bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());

        if (addressMap.keySet().size() != 2) {
             ApiMessage.ofStatus(ApiMessage.Status.NOT_VALID_PARAM);
        }

        ServiceResult result = houseService.update(houseForm);
        if (result.isSuccess()) {
            return ApiMessage.ofSuccess(null);
        }

        return ApiMessage.ofMessage(ApiMessage.Status.BAD_REQUEST.getCode(),result.getMessage());
    }

    /**
     * @param id
     * @return
     */
    @DeleteMapping("admin/house/photo")
    @ResponseBody
    public ResponseEntity removeHousePhoto(@RequestParam(value = "id") Long id) {
        ServiceResult result = this.houseService.removePhoto(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity(result.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * modify image cover
     * @param coverId
     * @param targetId
     * @return
     */
    @PostMapping("admin/house/cover")
    @ResponseBody
    public ResponseEntity updateCover(@RequestParam(value = "cover_id") Long coverId,
                                      @RequestParam(value = "target_id") Long targetId) {
        ServiceResult result = this.houseService.updateCover(coverId, targetId);

        if (result.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity(result.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * add tag
     * @param houseId
     * @param tag
     * @return
     */
    @PostMapping("admin/house/tag")
    @ResponseBody
    public ResponseEntity addHouseTag(@RequestParam(value = "house_id") Long houseId,
                                      @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || tag==null || tag.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.addTag(houseId, tag);
        if (result.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity(result.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * remove tag
     * @param houseId
     * @param tag
     * @return
     */
    @DeleteMapping("admin/house/tag")
    @ResponseBody
    public ResponseEntity removeHouseTag(@RequestParam(value = "house_id") Long houseId,
                                         @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || tag==null || tag.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.removeTag(houseId, tag);
        if (result.isSuccess()) {
            return ResponseEntity.ok().build();
        } else {
            return new ResponseEntity(result.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * verification
     * @param id
     * @param operation
     * @return
     */
    @PutMapping("admin/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiMessage operateHouse(@PathVariable(value = "id") Long id,
                                       @PathVariable(value = "operation") int operation) {
        if (id <= 0) {
            return ApiMessage.ofStatus(ApiMessage.Status.BAD_REQUEST);
        }
        ServiceResult result;

        switch (operation) {
            case HouseOperation.PASS:
                result = this.houseService.updateStatus(id, HouseStatus.PASSES.getValue());
                break;
            case HouseOperation.PULL_OUT:
                result = this.houseService.updateStatus(id, HouseStatus.NOT_AUDITED.getValue());
                break;
            case HouseOperation.DELETE:
                result = this.houseService.updateStatus(id, HouseStatus.DELETED.getValue());
                break;
            case HouseOperation.RENT:
                result = this.houseService.updateStatus(id, HouseStatus.RENTED.getValue());
                break;
            default:
                return ApiMessage.ofStatus(ApiMessage.Status.BAD_REQUEST);
        }

        if (result.isSuccess()) {
            return ApiMessage.ofSuccess(null);
        }
        return ApiMessage.ofStatus(ApiMessage.Status.BAD_REQUEST);
    }
}
