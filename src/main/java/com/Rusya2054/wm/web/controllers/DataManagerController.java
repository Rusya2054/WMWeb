package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.IndicatorInputDataValidator;
import com.Rusya2054.wm.web.files.parser.InputFileParser;
import com.Rusya2054.wm.web.files.reader.IndicatorReader;
import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import com.Rusya2054.wm.web.repositories.PumpCardRepository;
import com.Rusya2054.wm.web.repositories.WellRepository;
import com.Rusya2054.wm.web.services.IndicatorService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.Rusya2054.wm.web.files.parser.InputFileParser.parseIndicatorsFile;

@Controller
@RequiredArgsConstructor
public class DataManagerController {
    private final IndicatorRepository indicatorRepository;
    private final PumpCardRepository pumpCardRepository;
    private final WellRepository wellRepository;
    private final IndicatorService indicatorService;

    private final Map<Long, Map<String, List<String>>> sessionMemory;

    @GetMapping("/pump-card")
    public String getDataManager(Model model){
        // передача списка всех товаров
//        model.addAttribute("wells", indicatorRepository.findDistinctWells());

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
                    // TODO: добавить оповещение или логгирование
                    uploadedIndicatorsFiles.put(f.getOriginalFilename(), uploadedFile);
                    uploadedParsedIndicatorsFiles.put(f.getOriginalFilename(), InputFileParser.parseIndicatorsFile(uploadedFile, "\t", 20));
                }
            } catch (Exception e) {
                e.getMessage();
            }
        });
        if (!uploadedIndicatorsFiles.isEmpty()){
            Long sessionID = (long)(Long.MAX_VALUE*Math.random());
            sessionMemory.put(sessionID, uploadedIndicatorsFiles);
            model.addAttribute("sessionID", sessionID);
            model.addAttribute("separator", "\t");
            model.addAttribute("uploadedParsedIndicatorsFiles", uploadedParsedIndicatorsFiles);
            return "tabs";
        }
        return "data-manager";
    }

    @Data
    public static class RequestData {
        private String sessionID ;
        private String separator;
        private Boolean byFileName;
    }

    @PostMapping("/pump-card/upload/parse-indicators")
    @ResponseBody
    public Map<String, Object> dynamicParseIndicatorsFile(@RequestBody  RequestData requestData) {
        String separator = requestData.getSeparator();
        Long sessionID =Long.parseLong(requestData.getSessionID().replace(" ", ""));

        Map<String, Object> response = new HashMap<>();
        // TODO: логгирование по сессии если ошибки
        if (this.sessionMemory.keySet().contains(sessionID)){
            Map<String, List<String>> uploadedIndicatorsFiles =  this.sessionMemory.get(sessionID);
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
        }
        return response;
    }

    @PostMapping("/pump-card/upload/indicators")
    public String uploadFilesToDb(@RequestBody  RequestData requestData){
        class SeparatorValidator{

            public static String validate(String sep){
                if (sep.equals("\\t")){
                    return "\t";
                }
                if (sep.equals(".")){
                    return "\\.";
                }
                return sep;
            }
        }

        String separator = SeparatorValidator.validate(requestData.getSeparator());
        Boolean byFileName = requestData.getByFileName();
//        Boolean.valueOf()
        Long sessionID =Long.parseLong(requestData.getSessionID().replace(" ", ""));
        if (this.sessionMemory.keySet().contains(sessionID)){
//            InputFileParser.p
            Map<String, List<String>> uploadedIndicatorsFiles =  this.sessionMemory.get(sessionID);
            uploadedIndicatorsFiles.entrySet().forEach(e->{
                final Well well = new Well();
                if (byFileName) {
                    well.setName(e.getKey().split("\\.")[0]);
                }
                List<String> uploadedParsedIndicatorsFile =  InputFileParser.parseIndicatorsFile(e.getValue(), separator);
                List<Indicator> indicators = new ArrayList<>(uploadedParsedIndicatorsFile.size());
                uploadedParsedIndicatorsFile
                        .stream()
                        .skip(1)
                        .forEach(string -> {
                            String[] splittedData = string.split(separator);
                            Indicator indicator = IndicatorInputDataValidator.validate(splittedData);
                            if (well.getName() == null && !byFileName){
                                well.setName(splittedData[0]);
                            }
                            indicators.add(indicator);
                });
                Well dbWell = wellRepository.findByName(well.getName());
                if(dbWell == null){
                    wellRepository.save(well);
                    dbWell = well;
                }
                Well finalDbWell = dbWell;
                indicators.forEach(indicator -> indicator.setWell(finalDbWell));
                for (Indicator i : indicators){
                    try {
                        // TODO: сделать провеку по датам и добавлять
                        indicatorService.saveWithNewTransaction(i);
                    } catch (Exception ignore){

                    }
                }
            });
            sessionMemory.remove(sessionID);
        }
        // TODO: разобраться с перенаправлением на др страницу
        return "redirect:/pump-card";
    }

}
