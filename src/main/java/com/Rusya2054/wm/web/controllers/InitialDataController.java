package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.transfer.IndicatorWrapper;
import com.Rusya2054.wm.web.files.transfer.WellWrapper;
import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.PumpCardService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class InitialDataController {
    private final IndicatorService indicatorService;
    private final WellService wellService;
    private final PumpCardService pumpCardService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping("/init")
    public String getInitialData( Model model){
        List<Well> wellList = wellService.getWellList();
        List<WellWrapper> wellWrapperList = wellList.stream().map((well) -> {
            LocalDateTime maxDate = indicatorService.getIndicatorMaxDate(well);
            LocalDateTime minDate = indicatorService.getIndicatorMinDate(well);
            return new WellWrapper(well, minDate.format(formatter), maxDate.format(formatter));
        }).toList();
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
        System.out.println(indicators.size());
        // TODO: задать возможно выбрать интервал и чтобы он отображался и не слетал стиль

        model.addAttribute("well", well);
        model.addAttribute("pumpCard", pumpCard);
        model.addAttribute("indicators", indicators);
        return "initial-well";
    }

}
