package com.youseon.jpa.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CustomErrorController implements ErrorController {
	
	@RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public String accessDenied(HttpServletRequest request, HttpServletResponse response) throws IOException{
       return "errorController입장";
    }

    public String getErrorPath() {
        return "getErrorPath";
    }
}
