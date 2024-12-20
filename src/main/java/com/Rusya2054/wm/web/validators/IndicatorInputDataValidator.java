package com.Rusya2054.wm.web.validators;

import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Rusya2054
 */
public final class IndicatorInputDataValidator {


    public static Indicator validate(String[] stringList){
        Indicator indicator = new Indicator();
        indicator.setRotationDirection(stringList[1]);
        indicator.setDateTime(parseDateTime(stringList[2]));
        indicator.setFrequency(floatParser(stringList[3]));
        indicator.setCurPhaseA(floatParser(stringList[4]));
        indicator.setCurPhaseB(floatParser(stringList[5]));
        indicator.setCurPhaseC(floatParser(stringList[6]));
        indicator.setCurrentImbalance(Math.round(floatParser(stringList[7])));
        indicator.setLineCurrent(floatParser(stringList[8]));
        indicator.setLineVoltage(floatParser(stringList[9]));
        indicator.setActivePower(floatParser(stringList[10]));
        indicator.setTotalPower(floatParser(stringList[11]));
        indicator.setPowerFactor(floatParser(stringList[12]));
        indicator.setEngineLoad(floatParser(stringList[13]));
        indicator.setInputVoltageAB(floatParser(stringList[14]));
        indicator.setInputVoltageBC(floatParser(stringList[15]));
        indicator.setInputVoltageCA(floatParser(stringList[16]));
        indicator.setIntakePressure(floatParser(stringList[17]));
        indicator.setEngineTemp(floatParser(stringList[18]));
        indicator.setLiquidTemp(floatParser(stringList[19]));
        indicator.setVibrationAccRadial(floatParser(stringList[20]));
        indicator.setVibrationAccAxial(floatParser(stringList[21]));
        indicator.setLiquidFlowRatio(floatParser(stringList[22]));
        indicator.setIsolationResistance(floatParser(stringList[23]));
        return indicator;
    }

    private static Float floatParser(String s){
        try {
            float result;
            result = Float.parseFloat(s);
            return result;
        } catch (Exception ignore){
            return -1f;
        }
    }

    private static LocalDateTime parseDateTime(String s){
        // TODO: проверить работоспособность
        DateTimeFormatter[] formats = {DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")};
        final LocalDateTime[] value = {LocalDateTime.of(1970, 1, 1, 0, 0, 0)};
        Arrays.stream(formats).forEach(formatter -> {
            try {
                value[0] = LocalDateTime.parse(s, formatter);
            }
            catch (Exception ignore){
            }
        });
        return value[0];
    }

    public static Set<Indicator> formIndicator(List<String> uploadedParsedIndicatorsFile,
                                               String separator,
                                               Boolean byFileName,
                                               Well well){
        Set<Indicator> indicators = new TreeSet<>(Comparator.comparing(Indicator::getDateTime));

        uploadedParsedIndicatorsFile
                        .stream()
                        .skip(1)
                        .forEach(string -> {
                            String[] splittedData = string.split(separator);
                            Indicator indicator = IndicatorInputDataValidator.validate(splittedData);
                            if (well.getName() == null && !byFileName){
                                well.setName(splittedData[0]);
                            }
                            indicators.add(indicator);
                });
        return indicators;
    }

}
