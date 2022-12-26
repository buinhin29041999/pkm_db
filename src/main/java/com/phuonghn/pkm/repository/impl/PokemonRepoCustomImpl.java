package com.phuonghn.pkm.repository.impl;

import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.entity.Ability;
import com.phuonghn.pkm.entity.Type;
import com.phuonghn.pkm.repository.AbilityRepo;
import com.phuonghn.pkm.repository.PokemonRepoCustom;
import com.phuonghn.pkm.repository.TypeRepo;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PokemonRepoCustomImpl implements PokemonRepoCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;
    private final TypeRepo typeRepo;
    private final AbilityRepo abilityRepo;

    public PokemonRepoCustomImpl(TypeRepo typeRepo, AbilityRepo abilityRepo) {
        this.typeRepo = typeRepo;
        this.abilityRepo = abilityRepo;
    }

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
                "against_fairy,img_icon, img_large from pokemon where 1 = 1");
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
                    "against_psychic, against_bug, against_rock, against_ghost, against_dragon, against_dark, against_steel, against_fairy, img_icon," +
                    "img_large"), objects, PokemonDTO.class));
            total = ((BigInteger) countQuery.getSingleResult()).longValue();
        }
        rsDTOs = rsDTOs.stream()
                .peek(e -> {
                    try {
                        Path path = Paths.get(imgPokemonIcon + "/" + e.getImgIcon());
                        File file = new File(imgPokemonIcon + "/" + e.getImgIcon());
                        if (file.exists()) {
                            e.setImgIcon("data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(path)));
                        } else {
                            e.setImgIcon(null);
                        }

                        if (e.getType1() != null) {
                            Optional<Type> typeOptional1 = typeRepo.findById(e.getType1());
                            typeOptional1.ifPresent(e::setType1Entity);
                        }
                        if (e.getType2() != null) {
                            Optional<Type> typeOptional2 = typeRepo.findById(e.getType2());
                            typeOptional2.ifPresent(e::setType2Entity);
                        }
                        if (e.getAbility1() != null) {
                            Optional<Ability> abilityOptional1 = abilityRepo.findById(e.getAbility1());
                            abilityOptional1.ifPresent(e::setAbility1E);
                        }
                        if (e.getAbility2() != null) {
                            Optional<Ability> abilityOptional2 = abilityRepo.findById(e.getAbility2());
                            abilityOptional2.ifPresent(e::setAbility2E);
                        }
                        if (e.getAbilityHidden() != null) {
                            Optional<Ability> abilityOptional3 = abilityRepo.findById(e.getAbilityHidden());
                            abilityOptional3.ifPresent(e::setAbilityHiddenE);
                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.toList());
        return new PageImpl<>(rsDTOs, pageable, total);
    }
}
