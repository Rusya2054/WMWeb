package com.Rusya2054.wm.web.files.transfer;

import com.Rusya2054.wm.web.files.calculations.CalculationLiquidFlowRatioSmoothed;
import com.Rusya2054.wm.web.models.Indicator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Rusya2054
 */
@Slf4j
@Data
public class IndicatorMethodsInvoker {
    private static Integer lazyPlotParameter = 10;

    public static Map<LocalDateTime, Float> getDataByParameter1(String parameter, List<Indicator> indicators, Float inputLazyPlotCoeff){
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

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators, Float inputLazyPlotCoeff){
        final int inputLazyPlotParameter = (int) ((float)100/inputLazyPlotCoeff);
        // TODO: проверить на isEmpty()
        String methodNameToInvoke = "get"+parameter;
        Map<LocalDateTime, Float> result = new TreeMap<>(LocalDateTime::compareTo);

        if (parameter.endsWith("Smoothed")){
            result = CalculationLiquidFlowRatioSmoothed.getLiquidFlowRatioSmoothed(indicators);
            return result;
        }
        try {
            final Method method = Indicator.class.getMethod(methodNameToInvoke);
            Map<LocalDateTime, Float> subResult = IntStream.range(0, indicators.size())
                    .boxed()
                    .filter(index-> index%inputLazyPlotParameter == 0)
                    .collect(Collectors.toMap(
                            index-> indicators.get(index).getDateTime(),
                            index -> {
                                try {
                                    return (Float) method.invoke(indicators.get(index));
                                } catch (Exception ignore){
                                    return null;
                                }
                            },
                            (existing, replacement) -> existing
                    ));
            result.putAll(subResult);
        } catch (NoSuchMethodException e) {
            log.error("Method not found: {}", e.getMessage());
        }
        return result;
    }


}
