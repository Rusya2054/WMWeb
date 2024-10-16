package com.Rusya2054.wm.web.services;


import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.WellRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WellService {
    private final WellRepository wellRepository;

    public Well wellSave(Well well){
        List<Well> dbWellList = wellRepository.findByName(well.getName());
        if (dbWellList.isEmpty()){
            wellRepository.save(well);
            log.info("Saving new {}", well);
        }
        return well;
    }

    public List<Well> getWellsByName(Well well){
       return wellRepository.findByName(well.getName());
    }

    public Well getWell(Long id){
        return wellRepository.findById(id).orElse(new Well());
    }

    public Well getWell(Long id, String name){
        Well well = wellRepository.findById(id).orElse(new Well());
        if (well.getName() == null){
            well.setName(name);
            return wellRepository.save(well);
        }
        return well;
    }

    public List<Well> getWellList(){
        return wellRepository.findAll();
    }

    public String getTxtFileName(Long id){
        Well well = wellRepository.findById(id).orElse(null);
        if (well == null){
            return "";
        } else {
            return well.getName()+"_"+well.getField()+".txt";
        }
    }
}
