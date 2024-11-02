package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.controllers.request.RequestFieldData;
import com.Rusya2054.wm.web.controllers.request.RequestInitData;
import com.Rusya2054.wm.web.files.transfer.IndicatorWrapper;
import com.Rusya2054.wm.web.files.transfer.WellWrapper;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
/**
 * @author Rusya2054
 */
@Controller
@RequiredArgsConstructor
@SessionAttributes({"field"})
public class InitialDataController {
    private final IndicatorService indicatorService;
    private final WellService wellService;
    private final PumpCardService pumpCardService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/init")
    public String initWellsHandler(@RequestBody RequestFieldData requestFieldData, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("field", requestFieldData.getField());
        return "redirect:/init";
    }

    @GetMapping("/init")
    public String getInitialData(Model model){
        List<Well> wellList = wellService.getWellsByField((String) model.asMap().get("field"));
        model.addAttribute("wellList", wellList);
        return "initial";
    }

    @GetMapping("/init/{id}")
    public String getInitWellPage(@PathVariable Long id, Model model){
        Well well = wellService.getWell(id);
        PumpCard pumpCard = pumpCardService.getPumpCardByWell(well);
        List<IndicatorWrapper> indicators = indicatorService.getIndicatorWrapper(well).stream().limit(50).toList();
        model.addAttribute("well", well);
        model.addAttribute("pumpCard", pumpCard);
        model.addAttribute("indicators", indicators);
        // TODO: сессионные минимальные и максимальные даты
        model.addAttribute("wellWrapper", new WellWrapper(well,
                indicatorService.getIndicatorMinDate(well).format(formatter),
                indicatorService.getIndicatorMaxDate(well).format(formatter)));
        return "initial-well";
    }
    @PostMapping("/init/{id}")
    @ResponseBody
    public Map<String, List<IndicatorWrapper>> initWellPageUpdateData(@RequestBody RequestInitData requestInitData, @PathVariable Long id){
        Well well = wellService.getWell(id);
        if (!requestInitData.getMaxDate().isEmpty() || !requestInitData.getMaxDate().isEmpty()){
            LocalDateTime minDateTime = LocalDate.parse(requestInitData.getMinDate(), formatter).atStartOfDay();
            LocalDateTime maxDateTime = LocalDate.parse(requestInitData.getMaxDate(), formatter).plusDays(1L).atStartOfDay();
            LocalDateTime[] localDateTimes = DateTimeValidator.sortDateTimes(minDateTime, maxDateTime);
            LocalDateTime validatedMinDateTime = localDateTimes[0];
            LocalDateTime validatedMaxDateTime = localDateTimes[1];
            List<IndicatorWrapper> indicators = indicatorService.getIndicatorWrapper(well)
                    .stream()
                    .filter(ww->(ww.getDateTime().isAfter(validatedMinDateTime) && ww.getDateTime().isBefore(validatedMaxDateTime)))
                    .limit(requestInitData.getRowNumbers())
                    .toList();
            return new HashMap<>(1){{put("data", indicators);}};
        }
            return new HashMap<>(1){{put("data", List.of());}};
    }

}
