package com.Rusya2054.wm.web.validators;

import com.Rusya2054.wm.web.models.PumpCard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class InputPumpCardValidator {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
            pumpCard.setTypeSize(typeSize.trim());
        }
        String inputField = Optional.ofNullable(field).orElse("");
        String currentField = Optional.ofNullable(pumpCard.getWell().getField()).orElse("");
        if (!inputField.isEmpty() && !currentField.equalsIgnoreCase(inputField)){
            pumpCard.getWell().setField(inputField.trim());
        }

    }

    public static String getLastFailureDate(LocalDate localDate){
        if (localDate!= null) return localDate.format(dateTimeFormatter);
        else return null;
    }
}
