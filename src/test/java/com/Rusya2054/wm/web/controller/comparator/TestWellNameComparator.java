package com.Rusya2054.wm.web.controller.comparator;

import com.Rusya2054.wm.web.controllers.comparator.WellNameComparator;
import com.Rusya2054.wm.web.models.Well;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestWellNameComparator {
    private final WellNameComparator comparator = new WellNameComparator();

    @Test
    public void testComparator1(){
        Well well1 = new Well();
        well1.setName("123R");
        Well well2 = new Well();
        well2.setName("204-бис");
        Assertions.assertTrue(comparator.compare(well1, well2) < 0);
        Assertions.assertFalse(comparator.compare(well2, well1) < 0);
    }

    @Test
    public void testComparator2(){
        Well well1 = new Well();
        well1.setName("1");
        Well well2 = new Well();
        well2.setName("2");
        Assertions.assertTrue(comparator.compare(well1, well2) < 0);
        Assertions.assertFalse(comparator.compare(well2, well1) < 0);
    }

    @Test
    public void testComparator3(){
        Well well1 = new Well();
        well1.setName("");
        Well well2 = new Well();
        well2.setName("2");
        Assertions.assertTrue(comparator.compare(well1, well2) == 0);
        Assertions.assertFalse(comparator.compare(well2, well1) < 0);
    }

    @Test
    public void testComparator4(){
        Well well1 = new Well();
        well1.setName("dfg");
        Well well2 = new Well();
        well2.setName("gdfg");
        Assertions.assertTrue(comparator.compare(well1, well2) == 0);
        Assertions.assertFalse(comparator.compare(well2, well1) < 0);
    }

    @Test
    public void testComparator5(){
        Well well1 = new Well();
        well1.setName("");
        Well well2 = new Well();
        well2.setName("");
        Assertions.assertTrue(comparator.compare(well1, well2) == 0);
        Assertions.assertFalse(comparator.compare(well2, well1) < 0);
    }

    @Test
    public void testComparator6(){
        Well well1 = new Well();
        well1.setName(null);
        Well well2 = new Well();
        well2.setName(null);
        Assertions.assertTrue(comparator.compare(well1, well2) == 0);
        Assertions.assertFalse(comparator.compare(well2, well1) < 0);
    }
}
