package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.controllers.request.RequestFieldData;
import com.Rusya2054.wm.web.controllers.request.RequestVisualData;
import com.Rusya2054.wm.web.files.transfer.IndicatorMethodsInvoker;
import com.Rusya2054.wm.web.files.transfer.IndicatorWrapper;
import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.PumpCardService;
import com.Rusya2054.wm.web.services.WellService;
import com.Rusya2054.wm.web.validators.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequiredArgsConstructor
@SessionAttributes({"field"})
public class VisualizationController {
    private final IndicatorService indicatorService;
    private final WellService wellService;
    private final PumpCardService pumpCardService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @PostMapping("/visual")
    public String visualHandler(@RequestBody RequestFieldData requestFieldData, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("field", requestFieldData.getField());
        return "redirect:/visual";
    }

    @GetMapping("/visual")
    public String getVisualWells(Model model){
        List<Well> wellList = wellService.getWellsByField((String) model.asMap().get("field"));
        model.addAttribute("wellList", wellList);
        return "visual-wells";
    }

    @GetMapping("/visual/{id}")
    public String toVisual(@PathVariable Long id, Model model){
        Well well = wellService.getWell(id);
        PumpCard pumpCard = pumpCardService.getPumpCardByWell(well);
        List<Indicator> indicators = indicatorService.getIndicators(well).stream().limit(50).toList();

        model.addAttribute("well", well);
        model.addAttribute("pumpCard", pumpCard);
        model.addAttribute("indicators", indicators);
        if (pumpCard.getLastFailureDate() != null && pumpCard.getLastFailureDate() instanceof LocalDate){
            LocalDate lastFailureDate = pumpCard.getLastFailureDate();
            LocalDate today = LocalDate.now();
            Long daysBetween = ChronoUnit.DAYS.between(lastFailureDate, today);
            model.addAttribute("repairInterval", daysBetween);
        }
        return "visual";
    }

    @PostMapping("/visual/{id}")
    @ResponseBody
    public Map<LocalDateTime, Float> toVisual(@PathVariable Long id, @RequestBody RequestVisualData requestVisualData){
        Well well = wellService.getWell(id);
        if (requestVisualData.getMinDate().isEmpty() || requestVisualData.getMaxDate().isEmpty()){
            return IndicatorMethodsInvoker.getDataByParameter(requestVisualData.getParameter(), indicatorService.getIndicators(well));
        } else {
            LocalDateTime minDateTime = LocalDate.parse(requestVisualData.getMinDate(), formatter).atStartOfDay();
            LocalDateTime maxDateTime = LocalDate.parse(requestVisualData.getMaxDate(), formatter).plusDays(1L).atStartOfDay();
            LocalDateTime[] localDateTimes = DateTimeValidator.sortDateTimes(minDateTime, maxDateTime);
            LocalDateTime validatedMinDateTime = localDateTimes[0];
            LocalDateTime validatedMaxDateTime = localDateTimes[1];
            return IndicatorMethodsInvoker.getDataByParameter(requestVisualData.getParameter(), indicatorService.getIndicators(well),validatedMinDateTime, validatedMaxDateTime);
        }
    }
}
