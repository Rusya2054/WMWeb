package com.Rusya2054.wm.web.files.transfer;

import com.Rusya2054.wm.web.models.Indicator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Rusya2054
 */
@Slf4j
@Data
public class IndicatorMethodsInvoker {
    private static Integer lazyPlotParameter = 10;

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators, LocalDateTime minDate, LocalDateTime maxDate){
        String methodNameToInvoke = "get"+parameter;
        Map<LocalDateTime, Float> result = new TreeMap<>(LocalDateTime::compareTo);
        try {
            Method method = Indicator.class.getMethod(methodNameToInvoke);
            int[] counter = {0};
            indicators
                    .stream()
                    .filter(i->(i.getDateTime().isAfter(minDate) && i.getDateTime().isBefore(maxDate)))
                    .forEach(indicator -> {
                try {
                    LocalDateTime dateTime = indicator.getDateTime();
                    Float value = (Float) method.invoke(indicator);

                    if (counter[0] % lazyPlotParameter == 0) {
                        result.put(dateTime, value);
                    }
                    counter[0]++;
                } catch (Exception ignore) {
                }
            });
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {}", e.getMessage());
        }
        return result;
    }

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators){
        String methodNameToInvoke = "get"+parameter;
        Map<LocalDateTime, Float> result = new TreeMap<>(LocalDateTime::compareTo);
        try {
            Method method = Indicator.class.getMethod(methodNameToInvoke);
            int[] counter = {0};
            indicators.forEach(indicator -> {
                try {
                    LocalDateTime dateTime = indicator.getDateTime();
                    Float value = (Float) method.invoke(indicator);

                    if (counter[0] % lazyPlotParameter == 0) {
                        result.put(dateTime, value);
                    }
                    counter[0]++;
                } catch (Exception ignore) {
                }
            });
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {} for full history", e.getMessage());
        }
        return result;
    }

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators, Float inputLazyPlotCoeff){
        final int inputLazyPlotParameter = (int) ((float)100/inputLazyPlotCoeff);
        String methodNameToInvoke = "get"+parameter;
        Map<LocalDateTime, Float> result = new TreeMap<>(LocalDateTime::compareTo);
        try {
            Method method = Indicator.class.getMethod(methodNameToInvoke);
            int[] counter = {0};
            indicators.forEach(indicator -> {
                try {
                    LocalDateTime dateTime = indicator.getDateTime();
                    Float value = (Float) method.invoke(indicator);

                    if (counter[0] % inputLazyPlotParameter == 0) {
                        result.put(dateTime, value);
                    }
                    counter[0]++;
                } catch (Exception ignore) {
                }
            });
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {} for full history", e.getMessage());
        }
        return result;
    }

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators, LocalDateTime minDate, LocalDateTime maxDate, Float inputLazyPlotCoeff){
        final int inputLazyPlotParameter = (int) ((float)100/inputLazyPlotCoeff);
        String methodNameToInvoke = "get"+parameter;
        Map<LocalDateTime, Float> result = new TreeMap<>(LocalDateTime::compareTo);
        try {
            Method method = Indicator.class.getMethod(methodNameToInvoke);
            int[] counter = {0};
            indicators
                    .stream()
                    .filter(i->(i.getDateTime().isAfter(minDate) && i.getDateTime().isBefore(maxDate)))
                    .forEach(indicator -> {
                try {
                    LocalDateTime dateTime = indicator.getDateTime();
                    Float value = (Float) method.invoke(indicator);

                    if (counter[0] % inputLazyPlotParameter == 0) {
                        result.put(dateTime, value);
                    }
                    counter[0]++;
                } catch (Exception ignore) {
                }
            });
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {}", e.getMessage());
        }
        return result;
    }


}
