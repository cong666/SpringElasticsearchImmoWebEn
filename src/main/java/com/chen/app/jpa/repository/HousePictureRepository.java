package com.chen.app.jpa.repository;

import com.chen.app.jpa.model.HousePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ccong
 */
@Repository
public interface HousePictureRepository extends JpaRepository<HousePicture, Long> {

    List<HousePicture> findAllByHouseId(Long id);
}
