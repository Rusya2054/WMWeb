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
    public String uploadFilesToDb(@RequestParam("files[]") List<MultipartFile> files, Model model){
        Map<String, List<String>> uploadedIndicatorsFiles = new HashMap<>(files.size());
        Map<String, List<String>> uploadedParsedIndicatorsFiles = new HashMap<>(files.size());
        files.stream()
                .filter(f->(f.getOriginalFilename().endsWith(".txt")| f.getOriginalFilename().endsWith(".data")))
                .forEach(f->{
            try {
                List<String> uploadedFile = IndicatorReader.readIndicatorsFile(f);
                uploadedIndicatorsFiles.put(f.getOriginalFilename(), uploadedFile);
                // TODO: парсинг 20 строк добавить перегрузку
                uploadedParsedIndicatorsFiles.put(f.getOriginalFilename(), InputFileParser.parseIndicatorsFile(uploadedFile, "\t"));
            } catch (Exception e) {
                e.getMessage();
            }
        });
        Long sessionID = (long)(Long.MAX_VALUE*Math.random());
        sessionMemory.put(sessionID, uploadedIndicatorsFiles);
        model.addAttribute("sessionID", sessionID);
        model.addAttribute("separator", "\t");
        model.addAttribute("uploadedParsedIndicatorsFiles", uploadedParsedIndicatorsFiles);
        return "tabs";
    }

    @Data
    public static class RequestData {
        // TODO: разорабться с передаемаемым типом (не String a Long)
        private String sessionID ;
        private String separator;
    }

    @PostMapping("/pump-card/upload/parse-indicators")
    public String dynamicParseIndicatorsFile(@RequestBody  RequestData requestData, Model model) {
        String separator = requestData.getSeparator();
        Long sessionID =Long.parseLong(requestData.getSessionID().replace(" ", ""));
        System.out.println(separator +" " + sessionID);
        if (this.sessionMemory.keySet().contains(sessionID)){
            Map<String, List<String>> uploadedIndicatorsFiles =  this.sessionMemory.get(sessionID);
            Map<String, List<String>> uploadedParsedIndicatorsFiles = new HashMap<>(uploadedIndicatorsFiles.size());
            uploadedIndicatorsFiles
                    .entrySet()
                    .stream()
                    .forEach(e->{
                        uploadedParsedIndicatorsFiles.put(e.getKey(), InputFileParser.parseIndicatorsFile(e.getValue(), separator));
                    });
            model.addAttribute("sessionID", sessionID);
            model.addAttribute("separator", separator);
            model.addAttribute("uploadedParsedIndicatorsFiles", uploadedParsedIndicatorsFiles);
        }
        return "tabs ";
    }



}
