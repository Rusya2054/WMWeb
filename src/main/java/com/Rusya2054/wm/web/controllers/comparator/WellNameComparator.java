package com.Rusya2054.wm.web.controllers.comparator;

import com.Rusya2054.wm.web.models.Well;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WellNameComparator implements Comparator<Well> {
    private final Pattern wellNamePattern = Pattern.compile("\\d+");
    @Override
    public int compare(Well w1, Well w2) {
        if (w1 == null || w2 == null) return 0;
        if (w1.getName() == null || w2.getName() == null) return 0;
        Matcher wellMatcher1 = wellNamePattern.matcher(w1.getName());
        Matcher wellMatcher2 = wellNamePattern.matcher(w2.getName());
        Integer wellIntValue1 = null;
        Integer wellIntValue2 = null;
        if (wellMatcher1.find() && wellMatcher2.find()){
            wellIntValue1 = Integer.parseInt(wellMatcher1.group());
            wellIntValue2 = Integer.parseInt(wellMatcher2.group());
            return wellIntValue1.compareTo(wellIntValue2);
        }
        return 0;
    }
}
