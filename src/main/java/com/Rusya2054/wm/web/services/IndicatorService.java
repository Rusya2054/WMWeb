package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class IndicatorService {
    private final IndicatorRepository indicatorRepository;

    public void saveIndicators(Set<Indicator> indicators, Well well){
        indicators.forEach(indicator -> indicator.setWell(well));

        List<Indicator> dbIndicators = indicatorRepository.findByWell(well);
        Set<Indicator> indicatorsToUpload = new TreeSet<>(Comparator.comparing(Indicator::getDateTime));
        indicatorsToUpload.addAll(indicators);


         Map<LocalDateTime, Indicator> dbIndicatorMap = dbIndicators.stream()
        .collect(Collectors.toMap(Indicator::getDateTime, Function.identity(), (existing, replacement) -> existing));

        List<Indicator> toSave = indicatorsToUpload.stream()
            .filter(indicator -> !dbIndicatorMap.containsKey(indicator.getDateTime()))
            .collect(Collectors.toList());

        if (!toSave.isEmpty()) {
            indicatorRepository.saveAll(toSave);
        }
    }

    public LocalDateTime getIndicatorMaxDate(Well well){
        LocalDateTime maxDate = indicatorRepository.findMaxDate(well.getId());
        if (maxDate == null){
            return LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        } else {
            return maxDate;
        }
    }

    public LocalDateTime getIndicatorMinDate(Well well){
        LocalDateTime minDate = indicatorRepository.findMinDate(well.getId());
        if (minDate == null){
            return LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        } else {
            return minDate;
        }
    }
}
