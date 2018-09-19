package com.chen.immo.test.elasticsearch;

import com.chen.app.jpa.model.House;
import com.chen.app.search.HouseIndexTemplate;
import com.chen.immo.test.ApplicationTest;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by: ccong
 * Date: 18/9/7 下午2:49
 */
public class ModelMapperTestCase extends ApplicationTest {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testModelMapper() {
        House house = new House();
        house.setId(1l);
        house.setAdminId(2L);
        house.setTitle("my title");
        house.setPrice(200);
        house.setArea(300);
        HouseIndexTemplate hit = new HouseIndexTemplate();
        modelMapper.map(house, hit);
        System.out.println(hit.getHouseId()+","+hit.getTitle()+","+hit.getPrice()+","+hit.getArea());
    }
}
