package com.chen.app.jpa.repository;

import com.chen.app.jpa.model.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ccong
 */
@Repository
public interface SubwayStationRepository extends JpaRepository<SubwayStation, Long> {

    /**
     * find all station of one subway
     * @param subwayId
     * @return
     */
    List<SubwayStation> findAllBySubwayId(Long subwayId);
}
