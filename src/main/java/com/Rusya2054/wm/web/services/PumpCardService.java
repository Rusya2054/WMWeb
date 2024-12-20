package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import com.Rusya2054.wm.web.repositories.PumpCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rusya2054
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PumpCardService {
    private final PumpCardRepository pumpCardRepository;

    public void savePumpCard(PumpCard pumpCard){
        log.info("Saving new {}", pumpCard);
        pumpCardRepository.save(pumpCard);
    }
    private void deletePumpCard(Long id){
        pumpCardRepository.deleteById(id);
    }

    public PumpCard getPumpCardById(Long id) {
        // если не найден id то null
        return pumpCardRepository.findById(id).orElse(null);
    }
    public  PumpCard getPumpCardByWell(Well well){
        PumpCard pumpCard = pumpCardRepository.findByWell(well);
        if (pumpCard == null){
            pumpCard = new PumpCard().builder().well(well).build();
            pumpCardRepository.save(pumpCard);
            return pumpCard;
        } else {
            return pumpCard;
        }
    }

    public void deletePumpCard(Well well){
        PumpCard pumpCard = pumpCardRepository.findByWell(well);
        pumpCardRepository.delete(pumpCard);
    }
}
