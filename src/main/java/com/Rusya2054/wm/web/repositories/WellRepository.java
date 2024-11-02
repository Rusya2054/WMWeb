package com.Rusya2054.wm.web.repositories;

import com.Rusya2054.wm.web.models.Well;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rusya2054
 */
@Repository
public interface WellRepository extends JpaRepository<Well, Long> {

    List<Well> findByName(String name);

    @Query(value = "SELECT DISTINCT field FROM wells", nativeQuery = true)
    List<String> findUniqueFields();

    @Query(value = "SELECT w.id from wells w where w.field = :field ORDER BY w.id", nativeQuery = true)
    List<Long> findWellsByField(@Param("field") String field);
}
