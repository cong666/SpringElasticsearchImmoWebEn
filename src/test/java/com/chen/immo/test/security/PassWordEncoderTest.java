package com.chen.immo.test.security;

import com.chen.web.config.WebSecurityConfig;
import org.junit.Test;

/**
 * Created by: ccong
 * Date: 18/8/26 下午1:28
 */
public class PassWordEncoderTest {
    @Test
    public void testPassEncoder() {
        WebSecurityConfig config = new WebSecurityConfig();
        String pass = config.customPasswordEncoder().encode("ccong");
        System.out.println(pass);
    }
}
