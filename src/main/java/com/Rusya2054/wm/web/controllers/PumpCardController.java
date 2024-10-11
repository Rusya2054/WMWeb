package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.PumpCardService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class PumpCardController {
    private final PumpCardService pumpCardService;

    @Data
    public static class RequestPumpCardData{
        private String field;
        private String wellName;
        private String typeSize;
        private String pumpDepth;
        private String lastFailureDate;

    }

    @PostMapping("/pump-card/create")
    @ResponseBody
    public String pumpInfo(@RequestBody RequestPumpCardData requestPumpCardData){
        System.out.println("12321");
        return "pump-card";
    }
}
