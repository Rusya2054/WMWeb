package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.controllers.request.RequestFieldData;
import com.Rusya2054.wm.web.files.transfer.WellWrapper;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Rusya2054
 */
@Controller
@RequiredArgsConstructor
@SessionAttributes({"field"})
public class DataExportController {
    private final WellService wellService;
    private final IndicatorService indicatorService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/export")
    public String exportPageHandler(@RequestBody RequestFieldData requestFieldData, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("field", requestFieldData.getField());
        return "redirect:/export";
    }

    @GetMapping("/export")
    public String getExportPage(Model model){
        List<Well> wellList = wellService.getWellsByField((String) model.asMap().get("field"));
        List<WellWrapper> wellWrapperList = indicatorService.getWellWrappterFromDateTimeMinMaxMap(wellList, formatter);
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
