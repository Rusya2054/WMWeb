package com.Rusya2054.wm.web.validators;

public class SeparatorValidator{

    public static String validate(String sep){
        if (sep.equals("\\t")){
            return "\t";
        }
        if (sep.equals(".")){
            return "\\.";
        }
        return sep;
    }
}
