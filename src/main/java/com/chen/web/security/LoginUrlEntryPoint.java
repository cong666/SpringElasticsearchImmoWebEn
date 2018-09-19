package com.chen.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: ccong
 * Date: 18/8/26 上午11:39
 */
public class LoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private PathMatcher pathMatcher = new AntPathMatcher();

    private final Map<String, String> authEntryMap;

    public LoginUrlEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        authEntryMap = new HashMap<>();
        //user login entry point
        authEntryMap.put("/user/**", "/user/login");
        //admin login entry point
        authEntryMap.put("/admin/**", "/admin/login");
    }

    /**
     * redirect to the page according to the role
     * @param request
     * @param response
     * @param exception
     * @return
     */
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request,
          HttpServletResponse response, AuthenticationException exception) {
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        for (Map.Entry<String, String> auth : this.authEntryMap.entrySet()){
            if (this.pathMatcher.match(auth.getKey(), uri)){
                return auth.getValue();
            }
        }
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
}
