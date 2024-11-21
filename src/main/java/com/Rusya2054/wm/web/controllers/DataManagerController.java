package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.controllers.comparator.WellNameComparator;
import com.Rusya2054.wm.web.validators.IndicatorInputDataValidator;
import com.Rusya2054.wm.web.validators.SeparatorValidator;
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

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
/**
 * @author Rusya2054
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class DataManagerController {
    private final WellService wellService;
    private final IndicatorService indicatorService;
    private final SessionMemoryService sessionMemoryService;
    private final WellNameComparator wellNameComparator;

    @GetMapping("/pump-card")
    public String getDataManager(Model model){

        List<Well> wellList = wellService.getWellList().stream().peek(new Consumer<Well>() {
            @Override
            public void accept(Well well) {
                if (well.getField() == null){
                    well.setField("м-е отсутствует");
                }
            }
        }).collect(Collectors.toList());
        wellList.sort(wellNameComparator);
        wellList.sort(Comparator.comparing(Well::getField));
        model.addAttribute("wellList", wellList);
        return "data-manager";
    }

    @PostMapping("/pump-card/upload")
    public String uploadIndicators(@RequestParam(value = "files[]", required = false) List<MultipartFile> files, Model model){
        Long sessionID = sessionMemoryService.addToMemory(new HashMap<>(files.size()));
        Map<String, List<String>> uploadedParsedIndicatorsFiles = new HashMap<>(files.size());
        Iterator<MultipartFile> fileIterator = files.iterator();

        while (fileIterator.hasNext()){
            MultipartFile f = fileIterator.next();
            if (f.getOriginalFilename().endsWith(".txt")| f.getOriginalFilename().endsWith(".data")){
                List<String> uploadedFile = IndicatorReader.readIndicatorsFile(f, 21);
                if (uploadedFile.size() > 20){
                    uploadedParsedIndicatorsFiles.put(f.getOriginalFilename(), InputFileParser.parseIndicatorsFile(uploadedFile, "\t", 20));
                    // TODO: добавить байты
                    try {
                        sessionMemoryService.getSessionMemory().get(sessionID).put(f.getOriginalFilename(), f.getBytes());
                    } catch (IOException ioException){
                        log.warn("reading of file {} is throw IOException", f.getOriginalFilename());
                    }
                }
            }
            fileIterator.remove();
        }
        files.clear();
        if (!uploadedParsedIndicatorsFiles.isEmpty()){
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
        // TODO: протестировать
        String separator = requestData.getSeparator();
        Long sessionID =requestData.getSessionID();
        Map<String, Object> response = new HashMap<>();
        if (this.sessionMemoryService.getSessionMemory().keySet().contains(sessionID)){
            Map<String, byte[]> uploadedIndicatorsFiles =  this.sessionMemoryService.getSessionMemory().get(sessionID);

            Map<String, List<String>> uploadedParsedIndicatorsFiles = new HashMap<>(uploadedIndicatorsFiles.size());
            uploadedIndicatorsFiles
                    .entrySet()
                    .stream()
                    .forEach(e->{
                        List<String> stringsUploadedFiles = IndicatorReader.readIndicatorsFile(e.getValue());
                        uploadedParsedIndicatorsFiles.put(e.getKey(), InputFileParser.parseIndicatorsFile(stringsUploadedFiles, separator, 20));
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
            Map<String, byte[]> uploadedIndicatorsFiles =  this.sessionMemoryService.getSessionMemory().get(sessionID);
            Iterator<Map.Entry<String, byte[]>> uploadedIndicatorsFilesIterator = uploadedIndicatorsFiles.entrySet().iterator();

            while (uploadedIndicatorsFilesIterator.hasNext()){
                Map.Entry<String, byte[]> uploadedIndicatorsFilesEntry = uploadedIndicatorsFilesIterator.next();
                final Well well = new Well();
                if (byFileName) {
                    well.setName(uploadedIndicatorsFilesEntry.getKey().split("\\.")[0]);
                }
                List<String> stringsUploadedFiles = IndicatorReader.readIndicatorsFile(uploadedIndicatorsFilesEntry.getValue());
                Set<Indicator> indicators = IndicatorInputDataValidator.formIndicator(InputFileParser.parseIndicatorsFile(stringsUploadedFiles, separator),
                        separator, byFileName, well);
                List<Well> dbWellList = wellService.getWellsByName(well);
                if (!dbWellList.isEmpty()){
                    duplicateWellMap.put(uploadedIndicatorsFilesEntry.getKey(), dbWellList);
                } else{
                    Well finalDbWell = wellService.wellSave(well);
                    indicatorService.saveIndicators(indicators, finalDbWell);
                    stringsUploadedFiles.clear();
                    indicators.clear();
                    uploadedIndicatorsFilesIterator.remove();
                }
            }

            uploadedIndicatorsFiles.entrySet().forEach(e->{
                final Well well = new Well();
                if (byFileName) {
                    well.setName(e.getKey().split("\\.")[0]);
                }
                List<String> stringsUploadedFiles = IndicatorReader.readIndicatorsFile(e.getValue());
                Set<Indicator> indicators = IndicatorInputDataValidator.formIndicator(InputFileParser.parseIndicatorsFile(stringsUploadedFiles, separator),
                        separator, byFileName, well);
                List<Well> dbWellList = wellService.getWellsByName(well);
                if (!dbWellList.isEmpty()){
                    duplicateWellMap.put(e.getKey(), dbWellList);
                } else{
                    Well finalDbWell = wellService.wellSave(well);
                    indicatorService.saveIndicators(indicators, finalDbWell);
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
