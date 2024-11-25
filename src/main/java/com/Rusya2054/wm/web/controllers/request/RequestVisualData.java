package com.Rusya2054.wm.web.controllers.request;

import lombok.Data;

@Data
public class RequestVisualData {
    private String minDate;
    private String maxDate;
    private String parameter;
    private Float inputLazyPlotCoeff;
}
