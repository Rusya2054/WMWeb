package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.IndicatorInputDataValidator;
import com.Rusya2054.wm.web.files.SeparatorValidator;
import com.Rusya2054.wm.web.files.parser.InputFileParser;
import com.Rusya2054.wm.web.files.reader.IndicatorReader;
import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.services.IndicatorService;
import com.Rusya2054.wm.web.services.SessionMemoryService;
import com.Rusya2054.wm.web.services.WellService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DataManagerController {
    private final WellService wellService;
    private final IndicatorService indicatorService;
    private final SessionMemoryService sessionMemoryService;

    @GetMapping("/pump-card")
    public String getDataManager(Model model){
        model.addAttribute("wellList", wellService.getWellList());
        return "data-manager";
    }

    @PostMapping("/pump-card/upload")
    public String uploadIndicators(@RequestParam(value = "files[]", required = false) List<MultipartFile> files, Model model){
        Map<String, List<String>> uploadedIndicatorsFiles = new HashMap<>(files.size());
        Map<String, List<String>> uploadedParsedIndicatorsFiles = new HashMap<>(files.size());
        files.stream()
                .filter(f->(f.getOriginalFilename().endsWith(".txt")| f.getOriginalFilename().endsWith(".data")))
                .forEach(f->{
            try {
                List<String> uploadedFile = IndicatorReader.readIndicatorsFile(f);
                if (uploadedFile.size() > 20){
                    uploadedIndicatorsFiles.put(f.getOriginalFilename(), uploadedFile);
                    uploadedParsedIndicatorsFiles.put(f.getOriginalFilename(), InputFileParser.parseIndicatorsFile(uploadedFile, "\t", 20));
                }
            } catch (Exception e) {
                e.getMessage();
            }
        });
        if (!uploadedIndicatorsFiles.isEmpty()){
            Long sessionID = sessionMemoryService.addToMemory(uploadedIndicatorsFiles);
            model.addAttribute("sessionID", sessionID);
            model.addAttribute("separator", "\t");
            model.addAttribute("uploadedParsedIndicatorsFiles", uploadedParsedIndicatorsFiles);
            return "tabs";
        }
        model.addAttribute("wellList", wellService.getWellList());
        return "data-manager";
    }

    @Data
    public static class RequestData {
        private Long sessionID ;
        private String separator;
        private Boolean byFileName;
    }

    @PostMapping("/pump-card/upload/parse-indicators")
    @ResponseBody
    public Map<String, Object> dynamicParseIndicatorsFile(@RequestBody  RequestData requestData) {
        String separator = requestData.getSeparator();
        Long sessionID =requestData.getSessionID();
        Map<String, Object> response = new HashMap<>();
        if (this.sessionMemoryService.getSessionMemory().keySet().contains(sessionID)){
            Map<String, List<String>> uploadedIndicatorsFiles =  this.sessionMemoryService.getSessionMemory().get(sessionID);
            Map<String, List<String>> uploadedParsedIndicatorsFiles = new HashMap<>(uploadedIndicatorsFiles.size());
            uploadedIndicatorsFiles
                    .entrySet()
                    .stream()
                    .forEach(e->{
                        uploadedParsedIndicatorsFiles.put(e.getKey(), InputFileParser.parseIndicatorsFile(e.getValue(), separator, 20));
                    });
            response.put("sessionID", sessionID);
            response.put("separator", separator);
            response.put("uploadedParsedIndicatorsFiles", uploadedParsedIndicatorsFiles);
        } else {
            log.info("sessionID not founded");
        }
        // TODO: тут стиль слетает при взаимодействии
        return response;
    }

    @PostMapping("/pump-card/upload/indicators")
    public String uploadFilesToDb(@RequestBody  RequestData requestData, RedirectAttributes redirectAttributes){

        String separator = SeparatorValidator.validate(requestData.getSeparator());
        Boolean byFileName = requestData.getByFileName();
        Long sessionID = requestData.getSessionID();

        Map<String, List<Well>> duplicateWellMap = new HashMap<>(100);

        if (this.sessionMemoryService.getSessionMemory().containsKey(sessionID)){
            Map<String, List<String>> uploadedIndicatorsFiles =  this.sessionMemoryService.getSessionMemory().get(sessionID);
            uploadedIndicatorsFiles.entrySet().forEach(e->{
                final Well well = new Well();
                if (byFileName) {
                    well.setName(e.getKey().split("\\.")[0]);
                }
                Set<Indicator> indicators = IndicatorInputDataValidator.formIndicator(InputFileParser.parseIndicatorsFile(e.getValue(), separator),
                        separator, byFileName, well);
                List<Well> dbWellList = wellService.getWellsByName(well);
                if (!dbWellList.isEmpty()){
                    duplicateWellMap.put(e.getKey(), dbWellList);
                } else{
                    Well finalDbWell = wellService.wellSave(well);
                    indicatorService.saveIndicators(indicators, finalDbWell);
//                    this.sessionMemoryService.getSessionMemory().get(sessionID).remove(e.getKey());
                }
            });
        }
        if (duplicateWellMap.isEmpty()){
            this.sessionMemoryService.getSessionMemory().remove(sessionID);
            return "redirect:/pump-card";
        } else {
            redirectAttributes.addFlashAttribute("sessionID", sessionID);
            redirectAttributes.addFlashAttribute("separator", separator);
            redirectAttributes.addFlashAttribute("duplicateWellMap", duplicateWellMap);
            return "redirect:/well-dublicates";
        }
    }
}
