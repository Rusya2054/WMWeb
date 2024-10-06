package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.services.PumpCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class PumpCardController {
    private final PumpCardService pumpCardService;

    @GetMapping("/pump-card/{id}")
    public String pumpInfo(@PathVariable Long id, Model model){
        PumpCard pumpCard = pumpCardService.getPumpCardById(id);
        model.addAttribute("pump-card", pumpCard);
        return "pump-card";
    }
}
