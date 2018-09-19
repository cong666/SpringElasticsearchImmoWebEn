package com.chen.web.util;

import com.chen.app.jpa.model.Subway;
import com.chen.app.jpa.model.SubwayStation;
import com.chen.app.jpa.model.SupportAddress;
import com.chen.web.dto.SubwayDTO;
import com.chen.web.dto.SubwayStationDTO;
import com.chen.web.dto.SupportAddressDTO;

/**
 * Created by: ccong
 * Date: 18/8/26 下午9:12
 */
public class ModelMapper {
    public static SupportAddressDTO toSupportAddressDTO(SupportAddress supportAddress) {
        SupportAddressDTO dto = new SupportAddressDTO();
        dto.setId(supportAddress.getId());
        dto.setBelongTo(supportAddress.getBelongTo());
        dto.setCnName(supportAddress.getCnName());
        dto.setEnName(supportAddress.getEnName());
        dto.setLevel(supportAddress.getLevel());
        dto.setBaiduMapLatitude(supportAddress.getBaiduMapLatitude());
        dto.setBaiduMapLongitude(supportAddress.getBaiduMapLongitude());
        return dto;
    }

    public static SubwayDTO toSubwayDTO(Subway subway) {
        SubwayDTO dto = new SubwayDTO();
        dto.setId(subway.getId());
        dto.setCityEnName(subway.getCityEnName());
        dto.setName(subway.getName());
        return dto;
    }

    public static SubwayStationDTO toSubwayStationDTO(SubwayStation subwayStation) {
        SubwayStationDTO dto = new SubwayStationDTO();
        dto.setId(subwayStation.getId());
        dto.setName(subwayStation.getName());
        dto.setSubwayId(subwayStation.getSubwayId());
        return dto;
    }
}
