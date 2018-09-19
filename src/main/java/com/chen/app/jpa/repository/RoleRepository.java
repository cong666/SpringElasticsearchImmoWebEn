package com.chen.app.jpa.repository;


import com.chen.app.jpa.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by: ccong
 * Date: 18/8/25 下午5:11
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findRolesByUserId(Long userId);
}
