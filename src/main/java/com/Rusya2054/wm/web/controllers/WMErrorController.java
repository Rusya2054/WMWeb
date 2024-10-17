package com.Rusya2054.wm.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
public class WMErrorController implements ErrorController {

    @GetMapping("/error")
    public String errorHadler(HttpServletRequest request){

        // TODO: реализовать систему перенаправлений
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        System.out.println(statusCode);
        return "redirect:/";
    }
}
