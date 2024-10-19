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
        // TODO: добавить ассиноое вычисление максимальных и минимальных дат
        List<WellWrapper> wellWrapperList = indicatorService.createWellWrappers(wellList, formatter);
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
        return "initial";
    }

    @GetMapping("/init/{id}")
    public String getInitWellPage(@PathVariable Long id, Model model){
        Well well = wellService.getWell(id);
        PumpCard pumpCard = pumpCardService.getPumpCardByWell(well);
        List<IndicatorWrapper> indicators = indicatorService.getIndicatorWrapper(well).stream().limit(50).toList();
        // TODO: задать возможно выбрать интервал и чтобы он отображался и не слетал стиль
        model.addAttribute("well", well);
        model.addAttribute("pumpCard", pumpCard);
        model.addAttribute("indicators", indicators);
        model.addAttribute("wellWrapper", new WellWrapper(well,
                indicatorService.getIndicatorMinDate(well).format(formatter),
                indicatorService.getIndicatorMaxDate(well).format(formatter)));
        return "initial-well";
    }
    @PostMapping("/init/{id}")
    @ResponseBody
    public Map<Long, List<IndicatorWrapper>> initWellPageUpdateData(@RequestBody RequestInitData requestInitData, @PathVariable Long id){
        Well well = wellService.getWell(id);
        // TODO: проблема с передачей данных
        LocalDateTime minDate = LocalDate.parse(requestInitData.getMinDate(), formatter).atStartOfDay();
        LocalDateTime maxDate = LocalDate.parse(requestInitData.getMaxDate(), formatter).plusDays(1L).atStartOfDay();
        // TODO: добавить валидатор дат
        List<IndicatorWrapper> indicators = indicatorService.getIndicatorWrapper(well)
                .stream()
                .filter(ww->(ww.getDateTime().isAfter(minDate) && ww.getDateTime().isBefore(maxDate)))
                .limit(50)
                .toList();
        // TODO: задать возможно выбрать интервал и чтобы он отображался и не слетал стиль
        Map<Long, List<IndicatorWrapper>> resonseMap = new HashMap<>(1);
        resonseMap.put(id, indicators);
        return resonseMap;
    }

}
