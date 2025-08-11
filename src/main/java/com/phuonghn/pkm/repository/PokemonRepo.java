package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PokemonRepo extends JpaRepository<Pokemon, Long>, PokemonRepoCustom {

    @Query(value = "select p from Pokemon p where (:generationCode is null or p.generation = :generationCode)" +
            " and (:type is null or p.type1 = :type or p.type2 = :type) order by p.pokedexNumber")
    List<Pokemon> findAllByGen(@Param("generationCode") String generationCode,
                               @Param("type") String type);
 }
