package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.InputPumpCardValidator;
import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.PumpCardService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class WellController {
    private final WellService wellService;
    private final PumpCardService pumpCardService;


    @GetMapping("/well/{id}")
    public String getWellPage(@PathVariable Long id, Model model){
        Well well = wellService.getClienWell(id);
        PumpCard pumpCard = pumpCardService.getPumpCardByWell(well);
        if (well != null){
            if (pumpCard == null){
                pumpCardService.savePumpCard(PumpCard.builder().well(well).build());
                pumpCard = pumpCardService.getPumpCardByWell(well);
            }
            model.addAttribute("well", well);
            model.addAttribute("pumpCard", pumpCard);
            model.addAttribute("lastFailureDate", InputPumpCardValidator.getLastFailureDate(pumpCard.getLastFailureDate()));
            return "well-page";
        } else {
             model.addAttribute("wellList", wellService.getWellList());
             return "data-manager";
        }
    }

    @GetMapping("/well/{id}/update")
    public String updateWellData(@PathVariable Long id,
                                 @RequestParam("field") String field,
                                 @RequestParam("typeSize") String typeSize,
                                 @RequestParam("pumpDepth") String pumpDepth,
                                 @RequestParam("lastFailureDate") String lastFailureDate, Model model){
        Well well = wellService.getClienWell(id);
        PumpCard pumpCard = pumpCardService.getPumpCardByWell(well);
        InputPumpCardValidator.validate(pumpCard, field, typeSize, pumpDepth, lastFailureDate);
        pumpCardService.savePumpCard(pumpCard);
        // TODO: если ошибка то возвращение ошибки
        model.addAttribute("wellList", wellService.getWellList());
        return "data-manager";
    }
}
