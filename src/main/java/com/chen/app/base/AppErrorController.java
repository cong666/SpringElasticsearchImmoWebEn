package com.chen.app.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
public class AppErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @Autowired
    private ErrorAttributes errorAttributes;

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes){
        this.errorAttributes = errorAttributes;
    }

    /**
     * Error pages
     * @param response
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorPageHandler(HttpServletResponse response){
        Integer status = response.getStatus();
        switch(status){
            case 403 :
                return "403";
            case 404 :
                return "404";
            case 500 :
                return "500";
        }
        return "index";
    }

    /**
     * Error Process for Web service
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ERROR_PATH)
    public ApiMessage errorResponseHandler(HttpServletRequest request, WebRequest webRequest){

        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(webRequest, false);
        Integer status = getStatus(request);
        return new ApiMessage(status,String.valueOf(attr.getOrDefault("message", "error")),null);
    }

    private int getStatus(HttpServletRequest request){
        Integer status = (Integer)request.getAttribute("javax.servlet.error.status_code");
        return status != null ? status : 500;
    }

}
