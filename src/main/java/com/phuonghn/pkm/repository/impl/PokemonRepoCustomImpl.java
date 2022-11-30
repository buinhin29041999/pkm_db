package com.phuonghn.pkm.repository.impl;

import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.repository.PokemonRepoCustom;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class PokemonRepoCustomImpl implements PokemonRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<PokemonDTO> search(PokemonDTO pokemonDTO, Pageable pageable) {
        List<PokemonDTO> rsDTOs = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        StringBuilder count = new StringBuilder();
        HashMap<Object, Object> map = new HashMap<>();
        query.append("select id, pokedex_number, name, german_name, japanese_name, generation, status, species, type_number, " +
                "type_1, type_2, height_m, weight_kg, abilities_number, ability_1, ability_2, ability_hidden, total_points, " +
                "hp, attack, defense, sp_attack, sp_defense, speed, catch_rate, base_friendship, base_experience, growth_rate, " +
                "egg_type_number, egg_type_1, egg_type_2, percentage_male, egg_cycles, against_normal, against_fire, against_water, " +
                "against_electric, against_grass, against_ice, against_fight, against_poison, against_ground, against_flying, " +
                "against_psychic, against_bug, against_rock, against_ghost, against_dragon, against_dark, against_steel, " +
                "against_fairy from pokemon where 1 = 1");
        if (!DataUtils.isNullOrEmpty(pokemonDTO.getName())) {
            query.append(" and name = :name");
            map.put("name", DataUtils.makeLikeQuery(pokemonDTO.getName()));
        }
        if (pokemonDTO.getPokedexNumber() != null) {
            query.append(" and pokedex_number = :pokedexNumber");
            map.put("pokedexNumber", pokemonDTO.getPokedexNumber());
        }

        query.append(" order by pokedex_number");
        count.append("select count(*) from ( ").append(query).append(") as total");
        Query queryExecuted = entityManager.createNativeQuery(query.toString());
        Query countQuery = entityManager.createNativeQuery(count.toString());
        long total = 0L;
        map.forEach((k, v) -> {
            queryExecuted.setParameter(k.toString(), v);
            countQuery.setParameter(k.toString(), v);
        });
        queryExecuted.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<Object[]> objects = queryExecuted.getResultList();
        if (!objects.isEmpty()) {
            rsDTOs.addAll(DataUtils.convertListObjectsToClass(DataUtils.changeParamTypeSqlToJava("id, pokedex_number, " +
                    "name, german_name, japanese_name, generation, status, species, type_number, type_1, type_2, height_m, " +
                    "weight_kg, abilities_number, ability_1, ability_2, ability_hidden, total_points, hp, attack, defense, " +
                    "sp_attack, sp_defense, speed, catch_rate, base_friendship, base_experience, growth_rate, egg_type_number, " +
                    "egg_type_1, egg_type_2, percentage_male, egg_cycles, against_normal, against_fire, against_water, " +
                    "against_electric, against_grass, against_ice, against_fight, against_poison, against_ground, against_flying, " +
                    "against_psychic, against_bug, against_rock, against_ghost, against_dragon, against_dark, against_steel, against_fairy"), objects, PokemonDTO.class));
            total = ((BigInteger) countQuery.getSingleResult()).longValue();
        }
        return new PageImpl<>(rsDTOs, pageable, total);
    }
}
