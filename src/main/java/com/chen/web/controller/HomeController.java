package com.chen.web.controller;

import com.chen.app.base.ApiMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by: ccong
 * Date: 18/8/25 下午4:15
 */
@Controller
public class HomeController {

    @GetMapping(value = {"/","/index"})
    public String index(Model model) {
        model.addAttribute("name","EsImmo");
        return "index";
    }

    @GetMapping("/404")
    public String notFoundPage() {
        return "404";
    }

    @GetMapping("/403")
    public String accessError() {
        return "403";
    }

    @GetMapping("/500")
    public String internalError() {
        return "500";
    }

    @GetMapping("/logout/page")
    public String logoutPage() {
        return "logout";
    }

    @GetMapping("/test")
    @ResponseBody
    public ApiMessage test() {
        return ApiMessage.ofMessage(200,"we are success!");
    }
}
