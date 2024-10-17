package com.Rusya2054.wm.web.validators;

import java.time.LocalDateTime;
import java.util.Arrays;

public class DateTimeValidator {
    public static LocalDateTime[] sortDateTimes(LocalDateTime l1, LocalDateTime l2){
        LocalDateTime[] localDateTimes = {l1, l2};
        Arrays.sort(localDateTimes, LocalDateTime::compareTo);
        return localDateTimes;
    }
}
