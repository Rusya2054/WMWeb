package com.Rusya2054.wm.web.files.transfer;

import com.Rusya2054.wm.web.models.Indicator;
import lombok.Data;

import java.time.format.DateTimeFormatter;
/**
 * @author Rusya2054
 */
@Data
public class IndicatorWrapper extends Indicator {
    private String strDateTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Float defaulFloat = 0f;
    private Integer defautInteger = 0;
    private String defaulString = "";

     public IndicatorWrapper(Indicator i) {
        this.setId(i.getId());
        this.setWell(i.getWell());
        this.setRotationDirection(i.getRotationDirection() != null ? i.getRotationDirection() : defaulString);
        this.setDateTime(i.getDateTime());
        this.setFrequency(i.getFrequency() != null ? i.getFrequency() : defaulFloat);
        this.setIntakePressure(i.getIntakePressure() != null ? i.getIntakePressure() : defaulFloat);
        this.setCurPhaseA(i.getCurPhaseA() != null ? i.getCurPhaseA() : defaulFloat);
        this.setCurPhaseB(i.getCurPhaseB() != null ? i.getCurPhaseB() : defaulFloat);
        this.setCurPhaseC(i.getCurPhaseC() != null ? i.getCurPhaseC() : defaulFloat);
        this.setCurrentImbalance(i.getCurrentImbalance() != null ? i.getCurrentImbalance() : defautInteger);
        this.setLineCurrent(i.getLineCurrent() != null ? i.getLineCurrent() : defaulFloat);
        this.setLineVoltage(i.getLineVoltage() != null ? i.getLineVoltage() : defaulFloat);
        this.setActivePower(i.getActivePower() != null ? i.getActivePower() : defaulFloat);
        this.setTotalPower(i.getTotalPower() != null ? i.getTotalPower() : defaulFloat);
        this.setPowerFactor(i.getPowerFactor() != null ? i.getPowerFactor() : defaulFloat);
        this.setEngineLoad(i.getEngineLoad() != null ? i.getEngineLoad() : defaulFloat);
        this.setInputVoltageAB(i.getInputVoltageAB() != null ? i.getInputVoltageAB() : defaulFloat);
        this.setInputVoltageBC(i.getInputVoltageBC() != null ? i.getInputVoltageBC() : defaulFloat);
        this.setInputVoltageCA(i.getInputVoltageCA() != null ? i.getInputVoltageCA() : defaulFloat);
        this.setEngineTemp(i.getEngineTemp() != null ? i.getEngineTemp() : defaulFloat);
        this.setLiquidTemp(i.getLiquidTemp() != null ? i.getLiquidTemp() : defaulFloat);
        this.setVibrationAccRadial(i.getVibrationAccRadial() != null ? i.getVibrationAccRadial() : defaulFloat);
        this.setVibrationAccAxial(i.getVibrationAccAxial() != null ? i.getVibrationAccAxial() : defaulFloat);
        this.setLiquidFlowRatio(i.getLiquidFlowRatio() != null ? i.getLiquidFlowRatio() : defaulFloat);
        this.setIsolationResistance(i.getIsolationResistance() != null ? i.getIsolationResistance() : defaulFloat);
        this.strDateTime = i.getDateTime().format(formatter);
    }
}
