package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.repositories.PumpCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PumpCardService {
    private final PumpCardRepository pumpCardRepository;
        // TODO: поиск по скважине
//    public List<PumpCard> listPumpCards(String field){
//        if (field != null){
//            return pumpCardRepository.findByField(field);
//        }
//        return pumpCardRepository.findAll();
//    }

    private void savePumpCard(PumpCard pumpCard){
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
}
