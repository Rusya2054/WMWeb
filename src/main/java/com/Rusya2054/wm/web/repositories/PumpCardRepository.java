package com.Rusya2054.wm.web.repositories;

import com.Rusya2054.wm.web.models.PumpCard;
import com.Rusya2054.wm.web.models.Well;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PumpCardRepository extends JpaRepository<PumpCard, Long> {
    PumpCard findByWell(Well well);
}
