package com.Rusya2054.wm.web.repositories;

import com.Rusya2054.wm.web.models.Indicator;
import com.Rusya2054.wm.web.models.Well;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Rusya2054
 */
@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    List<Indicator> findByWell(Well well);

    @Query("SELECT DISTINCT i.well FROM Indicator i")
    List<Well> findDistinctWells();

    @Query(value = "SELECT MAX(i.date_time) FROM indicators i WHERE i.well_id = :wellId", nativeQuery = true)
    LocalDateTime findMaxDate(@Param("wellId") Long wellId);

    @Query(value = "SELECT MIN(i.date_time) FROM indicators i WHERE i.well_id = :wellId", nativeQuery = true)
    LocalDateTime findMinDate(@Param("wellId") Long wellId);

}
