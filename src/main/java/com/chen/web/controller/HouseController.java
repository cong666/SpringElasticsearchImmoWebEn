package com.chen.web.controller;

import com.chen.app.base.ApiMessage;
import com.chen.app.base.RentValueBlock;
import com.chen.app.jpa.model.SupportAddress;
import com.chen.app.search.SearchService;
import com.chen.app.service.AddressService;
import com.chen.app.service.HouseService;
import com.chen.app.service.UserService;
import com.chen.web.dto.*;
import com.chen.web.form.RentFilter;
import com.chen.web.util.ServiceMultipleResult;
import com.chen.web.util.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by: ccong
 * Date: 18/8/26 下午7:32
 */
@Controller
public class HouseController {
    private static final Logger logger = LoggerFactory.getLogger(HouseController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private SearchService searchService;

    @ResponseBody
    @GetMapping("rent/house/autocomplete")
    public ApiMessage autoComplete(@RequestParam(value="prefix") String prefix){
        if(prefix.isEmpty()){
            return ApiMessage.ofStatus(ApiMessage.Status.BAD_REQUEST);
        }
        ServiceResult<List<String>> result = searchService.suggest(prefix);
        return ApiMessage.ofSuccess(result.getResult());
    }

    @GetMapping("address/support/cities")
    @ResponseBody
    public ApiMessage getSupportCities() {
        ServiceMultipleResult<SupportAddressDTO> result = addressService.findAllCities();
        if(result.getResultSize() == 0) {
            return ApiMessage.ofMessage(ApiMessage.Status.NOT_FOUND.getCode(),ApiMessage.Status.NOT_FOUND.getStandarMessage());
        }

        return ApiMessage.ofSuccess(result.getResult());

    }

    /**
     * get regions
     * @param cityEnName
     * @return
     */
    @ResponseBody
    @RequestMapping("address/support/regions")
    public ApiMessage getSupportRegions(@RequestParam(name="city_name")String cityEnName){
        ServiceMultipleResult<SupportAddressDTO> addressResult = addressService.findAllRegionByCityName(cityEnName);
        if (addressResult.getResult() == null || addressResult.getTotal() < 1) {
            return ApiMessage.ofStatus(ApiMessage.Status.NOT_FOUND);
        }
        return ApiMessage.ofSuccess(addressResult.getResult());
    }

    /**
     * @param cityEnName
     * @return
     */
    @GetMapping("address/support/subway/line")
    @ResponseBody
    public ApiMessage getSupportSubwayLine(@RequestParam(name = "city_name") String cityEnName) {
        List<SubwayDTO> subways = addressService.findAllSubwayByCity(cityEnName);
        return ApiMessage.ofSuccess(subways);
    }

    /**
     * @param subwayId
     * @return
     */
    @GetMapping("address/support/subway/station")
    @ResponseBody
    public ApiMessage getSupportSubwayStation(@RequestParam(name = "subway_id") Long subwayId) {
        List<SubwayStationDTO> stationDTOS = addressService.findAllStationBySubway(subwayId);
        if (stationDTOS.isEmpty()) {
            return ApiMessage.ofStatus(ApiMessage.Status.NOT_FOUND);
        }

        return ApiMessage.ofSuccess(stationDTOS);
    }

    @GetMapping("rent/house")
    public String rentHousePage(@ModelAttribute RentFilter rentFilter,
                                Model model, HttpSession session,
                                RedirectAttributes redirectAttributes) {
        logger.debug("RentFilter : "+rentFilter);
        /*rentFilter is come from url request parm :
             http://localhost:8080/rent/house?cityEnName=bj&priceBlock=*-1000....
          method for creating the rul : rent-list.xhtml -> locate_url
        */
        if (rentFilter.getCityEnName() == null) {
            String cityEnNameInSession = (String) session.getAttribute("cityEnName");
            if (cityEnNameInSession == null) {
                redirectAttributes.addAttribute("msg", "must_chose_city");
                return "redirect:/index";
            } else {
                rentFilter.setCityEnName(cityEnNameInSession);
            }
        } else {
            session.setAttribute("cityEnName", rentFilter.getCityEnName());
        }

        ServiceResult<SupportAddressDTO> city = addressService.findCity(rentFilter.getCityEnName());
        if (!city.isSuccess()) {
            redirectAttributes.addAttribute("msg", "must_chose_city");
            return "redirect:/index";
        }
        model.addAttribute("currentCity", city.getResult());

        ServiceMultipleResult<SupportAddressDTO> addressResult = addressService.findAllRegionsByCityName(rentFilter.getCityEnName());
        if (addressResult.getResult() == null || addressResult.getTotal() < 1) {
            redirectAttributes.addAttribute("msg", "must_chose_city");
            return "redirect:/index";
        }

        ServiceMultipleResult<HouseDTO> serviceMultiResult = houseService.query(rentFilter);

        model.addAttribute("total", serviceMultiResult.getTotal());
        model.addAttribute("houses", serviceMultiResult.getResult());

        if (rentFilter.getRegionEnName() == null) {
            rentFilter.setRegionEnName("*");
        }

        model.addAttribute("searchBody", rentFilter);
        model.addAttribute("regions", addressResult.getResult());

        model.addAttribute("priceBlocks", RentValueBlock.PRICE_BLOCK);
        model.addAttribute("areaBlocks", RentValueBlock.AREA_BLOCK);

        model.addAttribute("currentPriceBlock", RentValueBlock.matchPrice(rentFilter.getPriceBlock()));
        model.addAttribute("currentAreaBlock", RentValueBlock.matchArea(rentFilter.getAreaBlock()));

        return "rent-list";
    }

    @GetMapping("rent/house/show/{id}")
    public String show(@PathVariable(value = "id") Long houseId,
                       Model model) {
        if (houseId <= 0) {
            return "404";
        }

        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(houseId);
        if (!serviceResult.isSuccess()) {
            return "404";
        }

        HouseDTO houseDTO = serviceResult.getResult();
        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());

        SupportAddressDTO city = addressMap.get(SupportAddress.Level.CITY);
        SupportAddressDTO region = addressMap.get(SupportAddress.Level.REGION);

        model.addAttribute("city", city);
        model.addAttribute("region", region);

        ServiceResult<UserDTO> userDTOServiceResult = userService.findById(houseDTO.getAdminId());
        model.addAttribute("agent", userDTOServiceResult.getResult());
        model.addAttribute("house", houseDTO);

       /* ServiceResult<Long> aggResult = searchService.aggregateDistrictHouse(city.getEnName(), region.getEnName(), houseDTO.getDistrict());
        model.addAttribute("houseCountInDistrict", aggResult.getResult());*/

        ServiceResult<Long> sr = searchService.aggregateDistrictHouse(city.getEnName(),region.getEnName(),houseDTO.getDistrict());
        model.addAttribute("houseCountInDistrict",sr.getResult());

        return "house-detail";
    }

    @GetMapping("rent/house/map")
    public String rentMapPage(@RequestParam(value="cityEnName") String cityEnName,
                              Model model,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        ServiceResult<SupportAddressDTO> city = addressService.findCity(cityEnName);
        if(!city.isSuccess()) {
            redirectAttributes.addAttribute("msg","must_choose_city");
            return "redirect:/index";
        } else {
            session.setAttribute("cityEnName",cityEnName);
            model.addAttribute("city",city.getResult());
            model.addAttribute("total",111);
            ServiceMultipleResult<SupportAddressDTO> regions = addressService.findAllRegionByCityName(cityEnName);
            model.addAttribute("regions",regions.getResult());
            return "rent-map";
        }
    }

}
