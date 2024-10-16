package com.Rusya2054.wm.web.files.transfer;

import com.Rusya2054.wm.web.models.Well;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WellWrapper extends Well {
    private String maxDate;
    private String minDate;
    public WellWrapper(Well well, String minDate, String maxDate){
        super();
        this.setId(well.getId());
        this.setField(well.getField());
        this.setName(well.getName());
        this.maxDate = maxDate;
        this.minDate = minDate;
    }
}
