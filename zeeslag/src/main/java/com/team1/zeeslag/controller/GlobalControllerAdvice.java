package com.team1.zeeslag.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class GlobalControllerAdvice {

    @ModelAttribute("userToken")
    public String getUserToken(HttpServletRequest request) {
        Object attribute = request.getAttribute("claims");
        if(attribute == null) return null;
        return attribute.toString();
    }
}
