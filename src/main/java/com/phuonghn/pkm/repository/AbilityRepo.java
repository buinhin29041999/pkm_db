package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Ability;
import com.phuonghn.pkm.service.dto.AbilityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AbilityRepo extends JpaRepository<Ability, Long> {
    Optional<Ability> findByName(String name);

    @Query("SELECT new com.phuonghn.pkm.service.dto.AbilityDTO(" +
            " a.id, a.name, a.description, a.descriptionVn, a.generation)" +
            " FROM Ability a" +
            " WHERE (:name IS NULL OR upper(a.name) LIKE concat('%', upper(:name), '%')) " +
            " AND (:generation IS NULL OR a.generation = :generation)" +
            " order by a.generation, a.name"
    )
    Page<AbilityDTO> search(String name, String generation, Pageable pageable);
}
