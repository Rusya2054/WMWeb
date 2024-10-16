package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class IndicatorService {
    private final IndicatorRepository indicatorRepository;
    private final WellService wellService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public String getIndicatorStringData(Long id, LocalDateTime minDateTime, LocalDateTime maxDateTime){
        Well well = wellService.getWell(id);
        LocalDateTime[] localDateTimes = {minDateTime, maxDateTime};
        Arrays.sort(localDateTimes, LocalDateTime::compareTo);
        LocalDateTime validatedMinDateTime = localDateTimes[0];
        LocalDateTime validatedMaxDateTime = localDateTimes[1];

        if (well.getId() != null){
            List<Indicator> indicators = indicatorRepository
                    .findByWell(well)
                    .stream()
                    .filter(i->(i.getDateTime().isAfter(validatedMinDateTime) && i.getDateTime().isBefore(validatedMaxDateTime)))
                    .toList();
            return indicatorToString(indicators);
        }
        return "";
    }

    public String indicatorToString(List<Indicator> indicators){
        StringBuilder stringBuilder = new StringBuilder(indicators.size());
        stringBuilder.append("wellID\tRotationDirection\tDate\tFrequency\tCurPhaseA\tCurPhaseB\tCurPhaseC\tCurrentImbalance\tLineCurrent\tLineVoltage\tActivePower\tTotalPower\tPowerFactor\tEngineLoad\tInputVoltageAB\tInputVoltageBC\tInputVoltageCA\tIntakePressure\tEngineTemp\tLiquidTemp\tVibrationAccRadial\tVibrationAccAxial\tliquidflowRatio\tisolationResistance\n");

        indicators.forEach(indicator -> {
            StringBuilder line = new StringBuilder(25);
            line.append(indicator.getWell().getName()).append('\t');
            line.append(indicator.getRotationDirection()).append('\t');
            line.append(indicator.getDateTime().format(formatter)).append('\t');
            line.append(indicator.getFrequency()).append('\t');
            line.append(indicator.getCurPhaseA()).append('\t');
            line.append(indicator.getCurPhaseB()).append('\t');
            line.append(indicator.getCurPhaseC()).append('\t');
            line.append(indicator.getCurrentImbalance()).append('\t');
            line.append(indicator.getLineCurrent()).append('\t');
            line.append(indicator.getLineVoltage()).append('\t');
            line.append(indicator.getActivePower()).append('\t');
            line.append(indicator.getTotalPower()).append('\t');
            line.append(indicator.getEngineLoad()).append('\t');
            line.append(indicator.getInputVoltageAB()).append('\t');
            line.append(indicator.getInputVoltageBC()).append('\t');
            line.append(indicator.getInputVoltageCA()).append('\t');
            line.append(indicator.getIntakePressure()).append('\t');
            line.append(indicator.getEngineTemp()).append('\t');
            line.append(indicator.getLiquidTemp()).append('\t');
            line.append(indicator.getVibrationAccRadial()).append('\t');
            line.append(indicator.getVibrationAccAxial()).append('\t');
            line.append(indicator.getLiquidFlowRatio()).append('\t');
            line.append(indicator.getIsolationResistance()).append('\t');
            line.append("\n");
            stringBuilder.append(line);
        });
        return stringBuilder.toString().replace("null", "-1");
    }
}
