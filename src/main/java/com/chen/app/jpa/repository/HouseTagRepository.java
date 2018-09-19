package com.chen.app.jpa.repository;

import com.chen.app.jpa.model.HouseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ccong
 */
@Repository
public interface HouseTagRepository extends JpaRepository<HouseTag, Long> {

    HouseTag findByNameAndHouseId(String name, Long houseId);

    List<HouseTag> findAllByHouseId(Long id);

    List<HouseTag> findAllByHouseIdIn(List<Long> houseIds);
}
