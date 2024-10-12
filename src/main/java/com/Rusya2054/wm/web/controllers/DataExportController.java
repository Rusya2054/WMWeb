package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DataExportController {
    private final WellService wellService;
    private final IndicatorService indicatorService;

    @GetMapping("/export")
    public String getExportPage(Model model){
        List<Well> wells = wellService.getWells();
        Map<Well, Map<String, LocalDateTime>> wellDateMap = new HashMap<>();
        wells.stream().forEach(well -> {
            System.out.println(indicatorService.getIndicatorDateTimeIntervals(well));
        });

        model.addAttribute("wells", wells);
        return "export";
    }

    @PostMapping("/export/filter")
    public String wellFilterHandler(Model model){

        return "";
    }

    @PostMapping("/export/filter1")
    public String fieldFilterHandler(Model model){

        return "";
    }
}
