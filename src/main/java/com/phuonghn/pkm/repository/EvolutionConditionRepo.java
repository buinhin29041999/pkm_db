package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.EvolutionCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvolutionConditionRepo extends JpaRepository<EvolutionCondition, Long> {

    List<EvolutionCondition> findAllByEvolutionId(Long id);
}
