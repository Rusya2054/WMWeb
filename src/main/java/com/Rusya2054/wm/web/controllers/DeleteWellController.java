package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import com.Rusya2054.wm.web.files.transfer.WellWrapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
@SessionAttributes({"wellWrapperList"})
public class DeleteWellController {
    private final IndicatorService indicatorService;
    private final WellService wellService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Data
    public static class RequestData{
        private Long id;
    }

    @PostMapping("/well/pre-delete")
    public String distributionDeleteWell(@RequestBody RequestData requestData, RedirectAttributes redirectAttributes){
        // TODO: добавить возможность получать весь список для удаляния(логика реализована, осталось вставить endPoind
        Long id = requestData.getId();
        Well well = wellService.getWell(id);

        if (id<0){
            List<WellWrapper> wellWrapperList = indicatorService.getWellWrappterFromDateTimeMinMaxMap(wellService.getWellList(), formatter);
            redirectAttributes.addFlashAttribute("wellWrapperList", wellWrapperList);
            return "redirect:/well/delete";
        } else if (well.getName() != null && !well.getName().isEmpty()) {
            List<WellWrapper> wellWrapperList = indicatorService.getWellWrappterFromDateTimeMinMaxMap(wellService.getWellsByName(well), formatter);
            redirectAttributes.addFlashAttribute("wellWrapperList", wellWrapperList);
            return "redirect:/well/delete";
        } else {
            return "redirect:/pump-card";
        }
    }
    @GetMapping("/well/delete")
    public String getDeleteWellPage(Model model){
        if (model.asMap().isEmpty()){
            model.addAttribute("wellList", wellService.getWellList());
            return "data-manager";
        } else {
            return "delete";
        }

    }

    @PostMapping("/well/delete-data")
    public String deleteWellData(@RequestBody Map<Long, DataExportController.RequestExportData> requestDeleteDataMap){
        requestDeleteDataMap.forEach((id, wellWrapper) -> {
            LocalDateTime minDateTime = LocalDate.parse(wellWrapper.getMinDate(), formatter).atStartOfDay();
            LocalDateTime maxDateTime = LocalDate.parse(wellWrapper.getMaxDate(), formatter).plusDays(1L).atStartOfDay();
            indicatorService.deleteIndicators(id, minDateTime, maxDateTime);
        });
        return "redirect:/pump-card";
    }



}
