package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.TypeEffect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeDetailRepo extends JpaRepository<TypeEffect, Long> {
    Optional<TypeEffect> findByAtkTypeIdAndDefTypeId(Long typeAId, Long typeBId);
}
