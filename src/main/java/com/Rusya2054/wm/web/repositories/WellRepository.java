package com.Rusya2054.wm.web.repositories;

import com.Rusya2054.wm.web.models.Well;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WellRepository extends JpaRepository<Well, Long> {

    List<Well> findByName(String name);
}
