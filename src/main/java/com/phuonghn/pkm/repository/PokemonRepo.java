package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PokemonRepo extends JpaRepository<Pokemon, Long>, PokemonRepoCustom {
}
