package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.entity.Pokemon;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PokemonRepo extends JpaRepository<Pokemon, Long>, PokemonRepoCustom {

    @Query(value = "select new com.phuonghn.pkm.service.dto.PokemonDTO(p.id, p.pokedexNumber, p.name) from Pokemon p where (:generationCode is null or p.generation = :generationCode)" +
            " and (:type is null or p.type1 = :type or p.type2 = :type) order by p.pokedexNumber")
    List<PokemonDTO> findAllByGen(@Param("generationCode") String generationCode,
                                  @Param("type") String type);
 }
