package com.Rusya2054.wm.web.files.calculations;

import com.Rusya2054.wm.web.models.Indicator;

public class IndicatorMeasurability {

    public static float getParameterMeasurability(Indicator i){
        int sum = 0;

        if (!i.getFrequency().equals(0f)) sum++;
        if (!i.getCurPhaseA().equals(0f)) sum++;
        if (!i.getCurPhaseB().equals(0f)) sum++;
        if (!i.getCurPhaseC().equals(0f)) sum++;
        if (!i.getCurrentImbalance().equals(0)) sum++;
        if (!i.getActivePower().equals(0f)) sum++;
        if (!i.getTotalPower().equals(0f)) sum++;
        if (!i.getPowerFactor().equals(0f)) sum++;
        if (!i.getEngineLoad().equals(0f)) sum++;
        if (!i.getInputVoltageAB().equals(0f)) sum++;
        if (!i.getInputVoltageBC().equals(0f)) sum++;
        if (!i.getInputVoltageCA().equals(0f)) sum++;
        if (!i.getLiquidFlowRatio().equals(0f)) sum++;
        if (!i.getIsolationResistance().equals(0f)) sum++;
        if (!i.getIntakePressure().equals(0f) || !i.getEngineTemp().equals(0f) || !i.getLiquidTemp().equals(0f)){
            sum += 2;

        }
        if (!i.getIntakePressure().equals(0f)) sum++;
        if (!i.getEngineTemp().equals(0f)) sum++;
        if (!i.getLiquidTemp().equals(0f)) sum++;


        return (float) sum/19;
    }
}
