package com.Rusya2054.wm.web.files;

import com.Rusya2054.wm.web.models.PumpCard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputPumpCardValidator {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static LocalDate lastFailureDateParser(String lastFailureDate){
        try {
            LocalDate result = LocalDate.parse(lastFailureDate, dateTimeFormatter);
            return result;
        } catch (Exception ignore){
            return null;
        }
    }
    private static Float pumpDepthParser(String pumpDepth){
        try {
            return Float.parseFloat(pumpDepth.replace(" ", ""));
        } catch (Exception ignore){
            return null;
        }
    }

    public static void validate(PumpCard pumpCard,
                                String field,
                                String typeSize,
                                String pumpDepth,
                                String lastFailureDate){
        LocalDate date = lastFailureDateParser(lastFailureDate);
        if (date != null){
            pumpCard.setLastFailureDate(date);
        }
        Float depthVal = pumpDepthParser(pumpDepth);
        if (depthVal != null){
            pumpCard.setPumpDepth(depthVal);
        }
        if (!typeSize.isEmpty()){
            pumpCard.setTypeSize(typeSize);
        }
        if (!field.isEmpty() && (pumpCard.getWell().getField() == null) || (pumpCard.getWell().getField() != null && !pumpCard.getWell().getField().equalsIgnoreCase(field)) ){
            pumpCard.getWell().setField(field);
        }

    }

    public static String getLastFailureDate(LocalDate localDate){
        if (localDate!= null) return localDate.format(dateTimeFormatter);
        else return null;
    }
}
