package com.chen.app.jpa.repository;

import com.chen.app.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by: ccong
 * Date: 18/8/25 下午5:11
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByName(String name);
}
