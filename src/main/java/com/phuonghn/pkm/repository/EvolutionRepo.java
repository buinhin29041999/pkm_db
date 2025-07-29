package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Evolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvolutionRepo extends JpaRepository<Evolution, Long> {

    @Query(value = "select * from evolution e where e.from_id = :pokemonId or e.to_id = :pokemonId", nativeQuery = true)
    List<Evolution> findAllEvolOfPokemon(Long pokemonId);
}
