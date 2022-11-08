package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepo extends JpaRepository<Type, Long>, TypeRepoCustom {
    Optional<Type> findByName(String name);
}
