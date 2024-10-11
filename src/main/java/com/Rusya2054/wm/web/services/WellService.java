package com.Rusya2054.wm.web.services;


import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.WellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WellService {
    private final WellRepository wellRepository;

    public Well wellSave(Well well){
        Well dbWell = wellRepository.findByName(well.getName());
        if (dbWell == null){
            wellRepository.save(well);
            dbWell = well;
        }
        return dbWell;
    }

    public List<Well> getWells(){
        return wellRepository.findAll();
    }

    public List<Well> getWells(String s){
        return wellRepository.findAll().stream().filter(well -> well.getName().startsWith(s)).toList();
    }
}
