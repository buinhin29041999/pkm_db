package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.TypeDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeDetailRepo extends JpaRepository<TypeDetail, Long> {
    Optional<TypeDetail> findByAtkTypeIdAndDefTypeId(Long typeAId, Long typeBId);
}
