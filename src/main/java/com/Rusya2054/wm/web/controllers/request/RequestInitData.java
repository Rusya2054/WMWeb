package com.Rusya2054.wm.web.controllers.request;

import lombok.Data;

@Data
public class RequestInitData {
    private String minDate;
    private String maxDate;
    private Long nRows;
}
