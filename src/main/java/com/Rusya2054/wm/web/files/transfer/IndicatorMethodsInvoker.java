package com.Rusya2054.wm.web.files.transfer;

import com.Rusya2054.wm.web.models.Indicator;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class IndicatorMethodsInvoker {
    private static Integer lazyPlotParameter = 10;

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators, LocalDateTime minDate, LocalDateTime maxDate){
        String methodNameToInvoke = "get"+parameter;
        // TODO: TreeMap добавить
        Map<LocalDateTime, Float> result = indicators
                .stream()
                .filter(i->(i.getDateTime().isAfter(minDate) && i.getDateTime().isBefore(maxDate)))
                .collect(Collectors.toMap(
                        i->i.getDateTime(),
                        i-> {
                            Class<?> clazz = i.getClass();
                            Method method = null;
                            try {
                                 method = clazz.getMethod(methodNameToInvoke);
                                return (Float) method.invoke(i);
                                } catch (Exception ignore) {
                                return 0f;
                            }
                        },
                        (existing, replace) -> existing,
                        HashMap::new
                ));
        return result;
    }

    public static Map<LocalDateTime, Float> getDataByParameter(String parameter, List<Indicator> indicators){
        String methodNameToInvoke = "get"+parameter;
        // TODO: TreeMap добавить
        Map<LocalDateTime, Float> result = indicators
                .stream()
                .limit(15)
                .collect(Collectors.toMap(
                        i->i.getDateTime(),
                        i-> {
                            Class<?> clazz = i.getClass();
                            Method method = null;
                            try {
                                 method = clazz.getMethod(methodNameToInvoke);
                                return (Float) method.invoke(i);
                                } catch (Exception ignore) {
                                return 0f;
                            }
                        },
                        (existing, replace) -> existing,
                        HashMap::new
                ));
        return result;
    }
}
