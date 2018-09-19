package com.chen.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by: ccong
 * Date: 18/8/26 下午4:06
 */
@Controller
public class UserController {

    @GetMapping("/user/login")
    public String userLoginPage() {
        return "user/login";
    }

    @GetMapping("/user/center")
    public String userCenterPage() {
        return "user/center";
    }
}
