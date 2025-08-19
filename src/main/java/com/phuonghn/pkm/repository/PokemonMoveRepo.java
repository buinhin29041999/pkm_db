package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.PokemonMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PokemonMoveRepo extends JpaRepository<PokemonMove, Long> {
    List<PokemonMove> findByPokemonId(Long pokemonId);
}
