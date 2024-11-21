package com.Rusya2054.wm.web.services;

import com.Rusya2054.wm.web.repositories.IndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RequiredArgsConstructor
@SpringBootTest
public class TestIndicatorService {

    private final IndicatorService indicatorService;

    @MockBean
    private IndicatorRepository indicatorRepository;

    @Test
    public void testGetParameterMeasurability(){

         indicatorService.getParameterMeasurability(102L);
    }
}
