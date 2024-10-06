package com.Rusya2054.wm.web.controllers;

import com.Rusya2054.wm.web.files.json.JsonFormatter;
import com.Rusya2054.wm.web.files.reader.IndicatorReader;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import com.Rusya2054.wm.web.repositories.PumpCardRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static com.Rusya2054.wm.web.files.parser.InputFileParser.parseIndicatorsFile;

@Controller
@RequiredArgsConstructor
public class DataManagerController {
    private final IndicatorRepository indicatorRepository;
    private final PumpCardRepository pumpCardRepository;

    @GetMapping("/pump-card")
    public String getDataManager( Model model){
        // передача списка всех товаров
//        model.addAttribute("wells", indicatorRepository.findDistinctWells());

        return "data-manager";
    }

    @PostMapping("/pump-card/upload")
    public String uploadFilesToDb(@RequestParam("files[]") List<MultipartFile> files, Model model){
        Map<String, List<String>> uplodedIndicatorsFiles = new HashMap<>(files.size());
        files.stream()
                .filter(f->(f.getOriginalFilename().endsWith(".txt")| f.getOriginalFilename().endsWith(".data")))
                .forEach(f->{
            System.out.println("*".repeat(10));
            System.out.println(f.getName());
            System.out.println(f.getContentType());
            System.out.println(f.getOriginalFilename());
            System.out.println(f.getSize());
            // TODO: создать статический репозиторий для хранения List<MultipartFile> files

            try {
                uplodedIndicatorsFiles.put(f.getOriginalFilename(), IndicatorReader.readIndicatorsFile(f));
            } catch (Exception e) {
                e.getMessage();
            }
        });
        // model.addAttribute("uploadedIndicatorsFiles", uplodedIndicatorsFiles);


        model.addAttribute("uploadedIndicatorsFiles", JsonFormatter.strListToJson(uplodedIndicatorsFiles));
        // model.addAttribute("uploadedIndicatorsFiles", jsonFiles);
        return "tabs";
    }

    @PostMapping("/pump-card/upload/parse-indicators")
    public String dynamicParseIndicatorsFile(@RequestBody RequestData requestData, Model model) {
        System.out.println("Успешно данные пришли: " + requestData.getSep());
        parseIndicatorsFile(requestData.getStrings(), requestData.getSep());
        // model.addAttribute("uplodedIndicatorsFiles", JsonFormatter.strListToJson());
        return "dsf";
    }

    @Data
    public static class RequestData {
        private List<String> strings;
        private String sep;
    }

}
