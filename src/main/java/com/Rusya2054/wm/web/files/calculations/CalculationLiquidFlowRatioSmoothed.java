package com.Rusya2054.wm.web.files.calculations;

import com.Rusya2054.wm.web.models.Indicator;

import java.security.KeyStore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CalculationLiquidFlowRatioSmoothed {
    public static Map<LocalDateTime, Float> getLiquidFlowRatioSmoothed(List<Indicator> indicators){
        Set<LocalDate> uniqueDays = indicators.stream().map(i->i.getDateTime().toLocalDate()).collect(Collectors.toSet());
        Map<LocalDateTime, List<Float>> uniqueDaysListMap = new HashMap<>(uniqueDays.size());
        Map<LocalDateTime, Float> averagedDateMap = new TreeMap<>(LocalDateTime::compareTo);
        uniqueDays.forEach(day->{
             List<Float> values = indicators.stream()
                     .filter(i -> i.getLiquidFlowRatio() > 0)
                     .filter(i -> i.getDateTime().toLocalDate().equals(day)).map(Indicator::getLiquidFlowRatio).toList();

            uniqueDaysListMap.put(day.atStartOfDay(), values);
        });
        uniqueDaysListMap.entrySet().forEach(e->{
            Map<Float, Integer> frequencyMap = e.getValue().stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            v->1,
                            Integer::sum));

            Map<Integer, Float> invertedFrequencyMap = frequencyMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getValue,
                            Map.Entry::getKey,
                            (a, b) -> a
                    ));

            if (frequencyMap.size() > 2){
                List<Integer> maxValues = frequencyMap.values().stream()
                        .sorted(Comparator.reverseOrder())
                        .limit(2)
                        .collect(Collectors.toList());
                if (((float)maxValues.get(0)/(float)maxValues.get(1)) >= 10.0f){
                    Float valueToFilter = invertedFrequencyMap.get(maxValues.get(0));
                    Float averagedValue = (float) e.getValue().stream().filter(v->!v.equals(valueToFilter))
                    .mapToDouble(Float::doubleValue).average().orElse(0.0);
                    averagedDateMap.put(e.getKey(), averagedValue);
                } else {
                    Float averagedValue = (float) e.getValue().stream()
                    .mapToDouble(Float::doubleValue).average().orElse(0.0);
                    averagedDateMap.put(e.getKey(), averagedValue);
                }
            }

        });
        return averagedDateMap;
    }
}
