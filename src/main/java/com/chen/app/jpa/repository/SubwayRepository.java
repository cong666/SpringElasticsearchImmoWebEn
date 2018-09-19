package com.chen.app.jpa.repository;

import com.chen.app.jpa.model.Subway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ccong
 */
@Repository
public interface SubwayRepository extends JpaRepository<Subway, Long> {

    /**
     * find subway by city
     * @param cityEnName
     * @return
     */
    List<Subway> findAllByCityEnName(String cityEnName);
}
