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

    @Query(value = "select new com.phuonghn.pkm.service.dto.PokemonDTO(p.id, p.pokedexNumber, p.name, p.imgIcon, p.type1, p.type2) from Pokemon p" +
            " where (p.ability1 = :abilityId or p.ability2 = :abilityId) order by p.pokedexNumber")
    List<PokemonDTO> findPkmWithNormalAbility(@Param("abilityId") Long abilityId);

    @Query(value = "select new com.phuonghn.pkm.service.dto.PokemonDTO(p.id, p.pokedexNumber, p.name, p.imgIcon, p.type1, p.type2) from Pokemon p" +
            " where p.abilityHidden = :abilityId order by p.pokedexNumber")
    List<PokemonDTO> findPkmWithHiddenAbility(@Param("abilityId") Long abilityId);

 }
