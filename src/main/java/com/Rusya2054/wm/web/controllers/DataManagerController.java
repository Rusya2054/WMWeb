package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.parser.InputFileParser;
import com.Rusya2054.wm.web.files.reader.IndicatorReader;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import com.Rusya2054.wm.web.repositories.PumpCardRepository;
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

    private final Map<Long, Map<String, List<String>>> sessionMemory;

    @GetMapping("/pump-card")
    public String getDataManager( Model model){
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
            // TODO: удаление сессии из памяти

        String separator = requestData.getSeparator();
        Boolean byFileName = requestData.getByFileName();
//        Boolean.valueOf()
        Long sessionID =Long.parseLong(requestData.getSessionID().replace(" ", ""));
        if (this.sessionMemory.keySet().contains(sessionID)){
//            InputFileParser.p
            Map<String, List<String>> uploadedIndicatorsFiles =  this.sessionMemory.get(sessionID);
            uploadedIndicatorsFiles.entrySet().forEach(e->{
                // TODO: парсим переводим в стандартный вид класса Indicator[]
                List<String> uploadedIndicatorsFile =  e.getValue();
            });
        }
        return "data-manager";
    }

}
