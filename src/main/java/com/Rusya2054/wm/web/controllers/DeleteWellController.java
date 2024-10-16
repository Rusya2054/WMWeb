package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import com.Rusya2054.wm.web.files.transfer.WellWrapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class DeleteWellController {
    private final IndicatorService indicatorService;
    private final WellService wellService;

    @Data
    public static class RequestData{
        private Long id;
    }

    @PostMapping("/well/delete/")
    public String deleteWell(@RequestBody RequestData requestData, Model model){
        System.out.println("requestData: " + requestData.getId());
        Long id = requestData.getId();
        Well well = wellService.getWell(id);
        if (id<0){
            List<Well> wellList = wellService.getWellList();
            model.addAttribute("wells", wellList);
            return "all wells to delete";
        } else if (well.getName() != null && !well.getName().isEmpty()) {
            List<Well> oneWell = wellService.getWellsByName(well);
            model.addAttribute("wells", oneWell);
            return "redirect: ";
        } else {
            List<Well> wellList = wellService.getWellList();
            model.addAttribute("wells", wellList);
            return "redirect:/pump-card";
        }


    }
}
