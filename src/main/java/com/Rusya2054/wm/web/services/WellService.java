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
        Well dbWell = wellRepository.findByName(well.getName());
        if (dbWell == null){
            wellRepository.save(well);
            log.info("Saving new {}", well);
            dbWell = well;
        }
        return dbWell;
    }

    public Well getClienWell(Long id){
        Well well = wellRepository.findById(id).orElse(null);
        if (well.getField() == null){
            well.setField("м-е отсутствует");
        }
        return well;
    }

    public List<Well> getWellList(){
        return wellRepository.findAll();
    }
}
