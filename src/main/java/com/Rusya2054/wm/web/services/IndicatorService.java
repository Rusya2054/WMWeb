package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class IndicatorService {
    private final IndicatorRepository indicatorRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveWithNewTransaction(Indicator indicator) {
        try {
            indicatorRepository.save(indicator);
        } catch (Exception ignore){

        }

    }

}
