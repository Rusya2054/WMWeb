package com.Rusya2054.wm.web.repositories;

import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    List<Indicator> findByWellId(Well well);

    @Query("SELECT DISTINCT i.well FROM Indicator i")
    List<Well> findDistinctWells();



}
