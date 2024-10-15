package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.export.WellWrapper;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<String, Object> exportToFiles(@RequestBody Map<Long, RequestExportData> requestExportDataMap){
        // TODO: заменить на Map<String, ResponseEntity<String>>
        // TODO: проверить с "\n"
        // TODO: indicatorService отправляем ID и интервалы,а получаем в StringBuilder
        Map<String, Object> exportMap = new HashMap<>(3);
        exportMap.put("1.txt", "file - 1");
        exportMap.put("2.txt", "file - 2");
        exportMap.put("777.txt", "file - 777");

        return exportMap;
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
