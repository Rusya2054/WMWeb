package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.files.calculations.IndicatorMeasurability;
import com.Rusya2054.wm.web.files.transfer.IndicatorWrapper;
import com.Rusya2054.wm.web.files.transfer.WellWrapper;
import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import com.Rusya2054.wm.web.validators.DateTimeValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rusya2054
 */
@Slf4j
@RequiredArgsConstructor
@Service
@EnableAsync
public class IndicatorService {
    private final IndicatorRepository indicatorRepository;
    private final WellService wellService;
    private final PumpCardService pumpCardService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<Indicator> getIndicators(Well well){
        return indicatorRepository.findByWell(well);
    }

    public List<Indicator> getIndicators(Well well, LocalDateTime minDateTime, LocalDateTime maxDateTime){
        return indicatorRepository.findByWell(well).stream().filter(i->(i.getDateTime().isAfter(minDateTime) && i.getDateTime().isBefore(maxDateTime))).toList();
    }

    public List<IndicatorWrapper> getIndicatorWrapper(Well well){
        return indicatorRepository.findByWell(well).stream().map(IndicatorWrapper::new).toList();
    }

    public void saveIndicators(Set<Indicator> indicators, Well well){
        indicators.forEach(indicator -> indicator.setWell(well));

        List<Indicator> dbIndicators = indicatorRepository.findByWell(well);
        Set<Indicator> indicatorsToUpload = new TreeSet<>(Comparator.comparing(Indicator::getDateTime));
        indicatorsToUpload.addAll(indicators);

         Map<LocalDateTime, Indicator> dbIndicatorMap = dbIndicators.stream()
        .collect(Collectors.toMap(Indicator::getDateTime, Function.identity(), (existing, replacement) -> replacement));

        List<Indicator> toSave = indicatorsToUpload.stream()
            .filter(indicator -> !dbIndicatorMap.containsKey(indicator.getDateTime()))
            .collect(Collectors.toList());


        if (!toSave.isEmpty()) {
            indicatorRepository.saveAll(toSave);
            log.info("Well: {} is saved", well);
        }
        dbIndicators.clear();
        indicatorsToUpload.clear();
        toSave.clear();
        dbIndicatorMap.clear();
    }

    public LocalDateTime getIndicatorMaxDate(Well well){
        LocalDateTime maxDate = indicatorRepository.findMaxDate(well.getId());
        if (maxDate == null){
            log.warn("Incorrect minimum DateTime in {}", well);
            return LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        } else {
            return maxDate;
        }
    }

    public float getParameterMeasurability(Long id){
        List<Indicator> indicators = indicatorRepository.findLastRowByWellId(id);
        if (!indicators.isEmpty()){
            return Math.round(IndicatorMeasurability.getParameterMeasurability(indicators.get(0))*100);
        }
        return Math.round(0.0f*100);
    }

    public List<WellWrapper> getWellWrappterFromDateTimeMinMaxMap(List<Well> wellList, DateTimeFormatter formatter) {

        return wellList.stream()
                .map(well -> {
                        LocalDateTime minDateTime = getIndicatorMinDate(well);
                        LocalDateTime maxDateTime = getIndicatorMaxDate(well);
                        return new WellWrapper(well, minDateTime.format(formatter), maxDateTime.format(formatter));
                    })
                .toList();
    }

    public List<WellWrapper> createWellWrappers(List<Well> wellList, DateTimeFormatter formatter) {
        List<CompletableFuture<WellWrapper>> futures = wellList.stream()
                .map(well -> {
                    CompletableFuture<LocalDateTime> maxDateFuture = this.getAsyncIndicatorMaxDate(well);
                    CompletableFuture<LocalDateTime> minDateFuture = this.getAsyncIndicatorMinDate(well);

                    return maxDateFuture.thenCombine(minDateFuture, (maxDate, minDate) -> {
                        String formattedMaxDate = (maxDate != null) ? maxDate.format(formatter) : "1970-01-01";
                        String formattedMinDate = (minDate != null) ? minDate.format(formatter) : "1970-01-01";
                        return new WellWrapper(well, formattedMinDate, formattedMaxDate);
                    });
                })
                .toList();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<LocalDateTime> getAsyncIndicatorMinDate(Well well){
        LocalDateTime minDate = indicatorRepository.findMinDate(well.getId());
        if (minDate == null){
            log.warn("Async Incorrect minimum DateTime in {}", well);
            return CompletableFuture.completedFuture(LocalDateTime.of(1970, 1, 1, 0, 0, 0));
        } else {
            return CompletableFuture.completedFuture(minDate);
        }
    }

    @Async
    public CompletableFuture<LocalDateTime> getAsyncIndicatorMaxDate(Well well){
        LocalDateTime maxDate = indicatorRepository.findMaxDate(well.getId());
        if (maxDate == null){
            log.warn("Async Incorrect maximum DateTime in {}", well);
            return CompletableFuture.completedFuture(LocalDateTime.of(1970, 1, 1, 0, 0, 0));
        } else {
            return CompletableFuture.completedFuture(maxDate);
        }
    }

    public LocalDateTime getIndicatorMinDate(Well well){
        LocalDateTime minDate = indicatorRepository.findMinDate(well.getId());
        if (minDate == null){
            log.warn("Incorrect maximum DateTime in {}", well);
            return LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        } else {
            return minDate;
        }
    }

    public void deleteIndicators(Long id, LocalDateTime minDateTime, LocalDateTime maxDateTime){
        Well well = wellService.getWell(id);
        LocalDateTime[] localDateTimes = DateTimeValidator.sortDateTimes(minDateTime, maxDateTime);
        LocalDateTime validatedMinDateTime = localDateTimes[0];
        LocalDateTime validatedMaxDateTime = localDateTimes[1];
        List<Indicator>indicators = indicatorRepository
                .findByWell(well)
                .stream()
                .filter(i->(i.getDateTime().isAfter(validatedMinDateTime) && i.getDateTime().isBefore(validatedMaxDateTime)))
                .toList();
        indicatorRepository.deleteAll(indicators);
        List<Indicator> dbIndicators = indicatorRepository.findByWell(well);
        if (dbIndicators.isEmpty()){
            pumpCardService.deletePumpCard(well);
            wellService.deleteWell(well);
            log.info("Deleted well {}", well);
        }
    }

    public String getIndicatorStringData(Long id, LocalDateTime minDateTime, LocalDateTime maxDateTime){
        Well well = wellService.getWell(id);
        LocalDateTime[] localDateTimes = DateTimeValidator.sortDateTimes(minDateTime, maxDateTime);
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
            line.append(indicator.getPowerFactor()).append('\t');
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
