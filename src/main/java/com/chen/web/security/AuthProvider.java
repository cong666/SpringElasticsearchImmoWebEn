package com.chen.web.security;

import com.chen.app.jpa.model.User;
import com.chen.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by: ccong
 * Date: 18/8/26 上午11:39
 */
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = (String)authentication.getCredentials();
        //compare with the db
        User user = userService.findByName(userName);
        if(user==null) {
            throw new AuthenticationCredentialsNotFoundException("authError");
        }

        /*use the passwordencoder to verify the validation of the password,
         make sure the password encoded is stored into the db*/
        if(passwordEncoder.matches(password,user.getPassword())){
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }
        throw new BadCredentialsException("authError");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
