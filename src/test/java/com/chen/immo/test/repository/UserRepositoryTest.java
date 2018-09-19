package com.chen.immo.test.repository;

import com.chen.app.jpa.model.User;
import com.chen.app.jpa.repository.UserRepository;
import com.chen.immo.test.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by: ccong
 * Date: 18/8/25 下午5:21
 */
public class UserRepositoryTest extends ApplicationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindOne() {
        User user = userRepository.getOne(1L);
        Assert.assertEquals(user.getId(),1L);
    }
}
