package com.chen.app.jpa.repository;

import com.chen.app.jpa.model.SupportAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ccong
 */
@Repository
public interface SupportAddressRepository extends JpaRepository<SupportAddress, Long> {

    /**
     * find by level : city or region
     * @param level
     * @return
     */
    List<SupportAddress> findAllByLevel(String level);

    SupportAddress findByEnNameAndLevel(String enName, String level);

    SupportAddress findByEnNameAndBelongTo(String enName, String belongTo);

    List<SupportAddress> findAllByLevelAndBelongTo(String level, String belongTo);

}
