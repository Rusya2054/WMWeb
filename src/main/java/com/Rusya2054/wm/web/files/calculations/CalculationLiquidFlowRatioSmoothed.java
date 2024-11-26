package com.Rusya2054.wm.web.files.calculations;

import com.Rusya2054.wm.web.models.Indicator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CalculationLiquidFlowRatioSmoothed {
    public static Map<LocalDateTime, Float> getLiquidFlowRatioSmoothed(List<Indicator> indicators){
//        final Map<LocalDateTime, Float> result = new TreeMap<>(LocalDateTime::compareTo);
//
//        final Map<LocalDateTime, Float> subResult = IntStream.range(0, indicators.size())
//                .boxed()
//                .filter(index-> index%inputLazyPlotParameter == 0)
//                .collect(Collectors.toMap(
//                        index-> indicators.get(index).getDateTime(),
//                        index -> indicators.get(index).getLiquidFlowRatio(),
//                        (existing, replacement) -> existing
//                ));
//        result.putAll(subResult);
        Set<LocalDate> uniqueDays = indicators.stream().map(i->i.getDateTime().toLocalDate()).collect(Collectors.toSet());
        Map<LocalDateTime, Float> averagedDateMap = new TreeMap<>(LocalDateTime::compareTo);
        uniqueDays.forEach(day->{
             double average = indicators.stream()
                     .filter(i -> i.getLiquidFlowRatio() > 0)
                     .filter(i -> i.getDateTime().toLocalDate().equals(day))
                     .mapToDouble(Indicator::getLiquidFlowRatio)
                     .average()
                     .orElse(0.0);

            averagedDateMap.put(day.atStartOfDay(), (float)average);
        });

        return averagedDateMap;
    }
}
