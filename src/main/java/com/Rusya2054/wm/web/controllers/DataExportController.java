package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.transfer.WellWrapper;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DataExportController {
    private final WellService wellService;
    private final IndicatorService indicatorService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @GetMapping("/export")
    public String getExportPage(Model model){
        List<Well> wellList = wellService.getWellList();
        List<WellWrapper> wellWrapperList = wellList.stream().map((well) -> {
            LocalDateTime maxDate = indicatorService.getIndicatorMaxDate(well);
            LocalDateTime minDate = indicatorService.getIndicatorMinDate(well);
            return new WellWrapper(well, minDate.format(formatter), maxDate.format(formatter));
        }).toList();
        model.addAttribute("wells", wellWrapperList);
        return "export";
    }

    @Data
    public static class RequestExportData {
        private String minDate;
        private String maxDate;
    }

    @PostMapping("/export/files")
    @ResponseBody
    public Map<String, String> exportToFiles(@RequestBody Map<Long, RequestExportData> requestExportDataMap){
        Map<String, String> exportMap = new HashMap<>(requestExportDataMap.size());
        requestExportDataMap.forEach((key, value) ->{
            LocalDateTime minDate = LocalDate.parse(value.getMinDate(), formatter).atStartOfDay();
            LocalDateTime maxDate = LocalDate.parse(value.getMaxDate(), formatter).atStartOfDay();

            exportMap.put(wellService.getTxtFileName(key), indicatorService.getIndicatorStringData(key, minDate, maxDate));
        });
        return exportMap;
    }

    @PostMapping("/export/filter")
    public String wellFilterHandler(Model model){

        return "";
    }
}
