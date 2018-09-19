package com.chen.immo.test.repository;

import com.chen.app.jpa.model.House;
import com.chen.app.jpa.repository.HouseRepository;
import com.chen.app.search.HouseIndexTemplate;
import com.chen.immo.test.ApplicationTest;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

/**
 * Created by: ccong
 * Date: 18/9/7 下午5:09
 */
public class HouseRepositoryTest extends ApplicationTest {

    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    @Transactional
    public void test() {
        House house = houseRepository.getOne(15l);
        HouseIndexTemplate houseIndexTemplate = new HouseIndexTemplate();
        modelMapper.map(house, houseIndexTemplate);
    }
}
