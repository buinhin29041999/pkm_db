package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Ability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbilityRepo extends JpaRepository<Ability, Long> {
    Optional<Ability> findByName(String name);
}
