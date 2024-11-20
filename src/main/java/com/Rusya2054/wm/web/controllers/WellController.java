package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.controllers.request.RequestFromData;
import com.Rusya2054.wm.web.files.reader.IndicatorReader;
import com.Rusya2054.wm.web.validators.IndicatorInputDataValidator;
import com.Rusya2054.wm.web.validators.InputPumpCardValidator;
import com.Rusya2054.wm.web.validators.SeparatorValidator;
import com.Rusya2054.wm.web.files.parser.InputFileParser;
import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.PumpCardService;
import com.Rusya2054.wm.web.services.SessionMemoryService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Rusya2054
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes({"sessionID", "separator", "duplicateWellMap", "from"})
public class WellController {
    private final WellService wellService;
    private final PumpCardService pumpCardService;
    private final IndicatorService indicatorService;
    private final SessionMemoryService sessionMemoryService;

    @PostMapping("/fields")
    public String handleFieldsPost(@RequestBody RequestFromData requestFieldData, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("from", requestFieldData.getFrom());
        return "redirect:/fields";
    }

    @GetMapping("/fields")
    public String getFieldsPage(Model model) {
        List<String> fields = wellService.getUniqueFields();
        model.addAttribute("fields", fields);
        return "fields";
    }


    @GetMapping("/well/{id}")
    public String getWellPage(@PathVariable Long id, Model model){
        Well well = wellService.getWell(id);
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
        Well well = wellService.getWell(id);
        PumpCard pumpCard = pumpCardService.getPumpCardByWell(well);
        InputPumpCardValidator.validate(pumpCard, field, typeSize, pumpDepth, lastFailureDate);
        pumpCardService.savePumpCard(pumpCard);
        model.addAttribute("wellList", wellService.getWellList());
        return "data-manager";
    }

    @GetMapping("/well-dublicates")
    public String showWellDublicates(Model model) {
        Long sessionID = (long)model.getAttribute("sessionID");
        if (sessionMemoryService.getSessionMemory().containsKey(sessionID)){
            return "well-dublicates";
        } else {
            log.warn("sessionID not founded");
            return "data-manager";
        }
    }

    @Data
    public static class RequestData {
        private Long id;
        private String name;
        private Long sessionID ;
        private String separator;
    }

    @PostMapping("/well-dublicates/upload")
    public String uploadWellDublicates(@RequestBody Map<String, RequestData> requestData, Model model){
        Long[] sessionID = {null};

        Iterator<Map.Entry<String, RequestData>> requestDataIterator = requestData.entrySet().iterator();

        while (requestDataIterator.hasNext()){
            Map.Entry<String, RequestData> iteratorEntry = requestDataIterator.next();
            sessionID[0] = iteratorEntry.getValue().getSessionID();
            String separator = SeparatorValidator.validate(iteratorEntry.getValue().getSeparator());
            if (sessionMemoryService.getSessionMemory().containsKey(sessionID[0])){
                Well well = wellService.getWell(iteratorEntry.getValue().getId(), iteratorEntry.getValue().getName());
                List<String> stringsUploadedFiles = IndicatorReader.readIndicatorsFile(this.sessionMemoryService.getSessionMemory().get(sessionID[0]).get(iteratorEntry.getKey()));
                Set<Indicator> indicators = IndicatorInputDataValidator.formIndicator(
                        InputFileParser.parseIndicatorsFile(stringsUploadedFiles, separator),
                        separator,
                        true,
                        well
                        );
                indicatorService.saveIndicators(indicators, well);
                stringsUploadedFiles.clear();
                indicators.clear();
            }
            requestDataIterator.remove();
        }
        sessionMemoryService.getSessionMemory().remove(sessionID[0]);
        model.asMap().clear();
        model.addAttribute("wellList", wellService.getWellList());
        return "redirect:/pump-card";
    }
}
