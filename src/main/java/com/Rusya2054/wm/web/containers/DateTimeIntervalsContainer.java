package com.Rusya2054.wm.web.containers;

import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DateTimeIntervalsContainer {
    private final WellService wellService;
    private final IndicatorService indicatorService;
    @Getter
    private static final Map<Long, Map<String, LocalDateTime>> dateTimeMinMaxMap = new HashMap<>();


    @PostConstruct
    private void initDateTimeMinMaxMap(){
        // TODO: оптимизировать запрос
        wellService.getWellList().forEach(well -> {
            Map<String, LocalDateTime> map = new HashMap<>(2){{
                put("minDateTime", indicatorService.getIndicatorMinDate(well));
                put("maxDateTime", indicatorService.getIndicatorMaxDate(well));
            }};
             dateTimeMinMaxMap.put(well.getId(), map);
        });
        log.info("DateTimeIntervalsContainer.dateTimeMinMaxMap initialized");
    }

    public static void updateDateTimeMinMaxMap(Well well, LocalDateTime minValue, LocalDateTime maxValue){
        if (dateTimeMinMaxMap.containsKey(well.getId())){
            Map<String, LocalDateTime> map = dateTimeMinMaxMap.get(well.getId());
            if (map.get("minDateTime").isAfter(minValue)){
                dateTimeMinMaxMap.get(well.getId()).put("minDateTime", minValue);
            }
            if (map.get("maxDateTime").isBefore(maxValue)){
                dateTimeMinMaxMap.get(well.getId()).put("maxDateTime", maxValue);
            }
        } else {
            dateTimeMinMaxMap.put(well.getId(), new HashMap<>(2){{
                put("minDateTime", minValue);
                put("maxDateTime", maxValue);
            }});
        }
    }
}
