package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InitialDataController {
    private final IndicatorRepository indicatorRepository;

    @GetMapping("/init")
    public String getInitialData( Model model){
        // передача списка всех товаров
        model.addAttribute("wells", indicatorRepository.findDistinctWells());

        return "initial";
    }

}
