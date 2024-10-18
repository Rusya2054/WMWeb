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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DataExportController {
    private final WellService wellService;
    private final IndicatorService indicatorService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/export")
    public String getExportPage(Model model){
        // TODO: медленно
        LocalDateTime start1 = LocalDateTime.now();
        List<Well> wellList = wellService.getWellList();
        List<WellWrapper> wellWrapperList = indicatorService.createWellWrappers(wellList, formatter);
        System.out.println(LocalDateTime.now());
        System.out.println(start1);
        LocalDateTime start2 = LocalDateTime.now();
        Map<String, List<WellWrapper>> wellListMap = wellList
                .stream()
                .map(well -> {
                    var field = well.getField();
                    return Objects.requireNonNullElse(field, "м-е отсутствует");
                })
                .distinct()
                .collect(Collectors
                        .toMap(key -> key,
                                key ->
                                wellWrapperList
                                .stream()
                                .filter(well -> {
                                    return Objects.requireNonNullElse(well.getField(), "м-е отсутствует").equals(key);
                                    }).toList(),
                                (existing, replacement) -> existing,
                                () -> new TreeMap<String, List<WellWrapper>>(String::compareTo)
                                ));
        System.out.println(LocalDateTime.now());
        System.out.println(start2);
        // model.addAttribute("wellListMap", new HashMap<>());
        model.addAttribute("wellListMap", wellListMap);
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
            LocalDateTime maxDate = LocalDate.parse(value.getMaxDate(), formatter).plusDays(1L).atStartOfDay();

            exportMap.put(wellService.getTxtFileName(key), indicatorService.getIndicatorStringData(key, minDate, maxDate));
        });
        return exportMap;
    }

    @PostMapping("/export/filter")
    public String wellFilterHandler(Model model){

        return "";
    }
}
