package com.chen.app.base;

import com.chen.app.jpa.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserUtil {

    /**
     * get login user
     * @return
     */
    public static User load(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal != null && principal instanceof User){
            return (User)principal;
        }
        return null;
    }

    public static Long getLoginUser(){
        User user = load();
        if(user == null){
            return -1L;
        }
        return user.getId();
    }
}
