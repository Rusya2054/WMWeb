package com.Rusya2054.wm.web.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Data
@Service
@RequiredArgsConstructor
public class SessionMemoryService {
    private final Map<Long, Map<String, List<String>>> sessionMemory;

    public Long addToMemory(Map<String, List<String>> uploadedIndicatorsFiles){
        Long sessionID = (long)(Long.MAX_VALUE*Math.random());
        sessionMemory.put(sessionID, uploadedIndicatorsFiles);
        return sessionID;
    }
}
