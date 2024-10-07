package com.Rusya2054.wm.web.controllers;

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

    private final Map<Long, List<MultipartFile>> sessionMemory;

    @GetMapping("/pump-card")
    public String getDataManager( Model model){
        // передача списка всех товаров
//        model.addAttribute("wells", indicatorRepository.findDistinctWells());

        return "data-manager";
    }

    @PostMapping("/pump-card/upload")
    public String uploadFilesToDb(@RequestParam("files[]") List<MultipartFile> files, Model model){
        Map<String, List<String>> uploadedIndicatorsFiles = new HashMap<>(files.size());
        files.stream()
                .filter(f->(f.getOriginalFilename().endsWith(".txt")| f.getOriginalFilename().endsWith(".data")))
                .forEach(f->{
            System.out.println("*".repeat(10));
            System.out.println(f.getName());
            System.out.println(f.getContentType());
            System.out.println(f.getOriginalFilename());
            System.out.println(f.getSize());
            // TODO: создать статический репозиторий для хранения List<MultipartFile> files
            // TODO: улучитить производительность
            try {
                uploadedIndicatorsFiles.put(f.getOriginalFilename(), parseIndicatorsFile(IndicatorReader.readIndicatorsFile(f, 20), "\t"));
            } catch (Exception e) {
                e.getMessage();
            }
        });
        // model.addAttribute("uploadedIndicatorsFiles", uploadedIndicatorsFiles);
        Long sessionID = (long)(Long.MAX_VALUE*Math.random());
        sessionMemory.put(sessionID, files);
        model.addAttribute("sessionID", sessionID);
        model.addAttribute("separator", "\t");
        model.addAttribute("uploadedIndicatorsFiles", uploadedIndicatorsFiles);
        // model.addAttribute("uploadedIndicatorsFiles", jsonFiles);
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
        Map<String, List<String>> uploadedIndicatorsFiles = new HashMap<>(100);

        if (this.sessionMemory.keySet().contains(sessionID)){
            List<MultipartFile> files = this.sessionMemory.get(sessionID);

            files.stream()
                .filter(f->(f.getOriginalFilename().endsWith(".txt")| f.getOriginalFilename().endsWith(".data")))
                .forEach(f->{
            // TODO: улучитить производительность
            try {
                uploadedIndicatorsFiles.put(f.getOriginalFilename(), parseIndicatorsFile(IndicatorReader.readIndicatorsFile(f, 20), separator));
            } catch (Exception e) {
                e.getMessage();
            }
                });
        }
        model.addAttribute("sessionID", sessionID);
        model.addAttribute("separator", separator);
        model.addAttribute("uploadedIndicatorsFiles", uploadedIndicatorsFiles);
        return "tabs";
    }



}
