package com.Rusya2054.wm.web.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author Rusya2054
 */
@Controller
@RequiredArgsConstructor
public class MainPageController {

    @GetMapping("/")
    public String products(){
        return "main";
    }
}
