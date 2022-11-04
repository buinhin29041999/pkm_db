package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeDetailRepo extends JpaRepository<Type, Long> {
}
