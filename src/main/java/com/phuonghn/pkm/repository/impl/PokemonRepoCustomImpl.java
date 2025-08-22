package com.phuonghn.pkm.repository.impl;

import com.phuonghn.pkm.common.exeption.BusinessException;
import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.entity.Ability;
import com.phuonghn.pkm.entity.Type;
import com.phuonghn.pkm.repository.AbilityRepo;
import com.phuonghn.pkm.repository.PokemonRepoCustom;
import com.phuonghn.pkm.repository.TypeRepo;
import com.phuonghn.pkm.service.dto.AbilityDTO;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.mapper.AbilityMapper;
import com.phuonghn.pkm.service.mapper.TypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PokemonRepoCustomImpl implements PokemonRepoCustom {
    private final TypeRepo typeRepo;
    private final AbilityRepo abilityRepo;
    @PersistenceContext
    EntityManager entityManager;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private AbilityMapper abilityMapper;

    public PokemonRepoCustomImpl(TypeRepo typeRepo, AbilityRepo abilityRepo) {
        this.typeRepo = typeRepo;
        this.abilityRepo = abilityRepo;
    }

    @Override
    public Page<PokemonDTO> search(PokemonDTO pokemonDTO, Pageable pageable) {
        try {
            List<PokemonDTO> rsDTOs = new ArrayList<>();
            StringBuilder query = new StringBuilder();
            StringBuilder count = new StringBuilder();
            HashMap<Object, Object> map = new HashMap<>();
            query.append("select id, pokedex_number, name, german_name, japanese_name, generation, status, species, type_number, " +
                    "type_1, type_2, height_m, weight_kg, abilities_number, ability_1, ability_2, ability_hidden, total_points, " +
                    "hp, attack, defense, sp_attack, sp_defense, speed, catch_rate, base_friendship, base_experience, growth_rate, " +
                    "egg_type_number, egg_type_1, egg_type_2, percentage_male, egg_cycles, against_normal, against_fire, against_water, " +
                    "against_electric, against_grass, against_ice, against_fighting, against_poison, against_ground, against_flying, " +
                    "against_psychic, against_bug, against_rock, against_ghost, against_dragon, against_dark, against_steel, " +
                    "against_fairy,img_icon,img_large from pokemon where 1 = 1 and default_skin = 1");
            if (!DataUtils.isNullOrEmpty(pokemonDTO.getName())) {
                query.append(" and name = :name");
                map.put("name", DataUtils.makeLikeQuery(pokemonDTO.getName()));
            }
            if (!DataUtils.isNullOrEmpty(pokemonDTO.getPokedexNumber())) {
                query.append(" and pokedex_number = :pokedexNumber");
                map.put("pokedexNumber", pokemonDTO.getPokedexNumber());
            }
            if (!DataUtils.isNullOrEmpty(pokemonDTO.getGeneration())) {
                query.append(" and generation = :generation");
                map.put("generation", pokemonDTO.getGeneration());
            }
            if (!DataUtils.isNullOrEmpty(pokemonDTO.getType())) {
                query.append(" and (type_1 = :type or type_2 = :type)");
                map.put("type", pokemonDTO.getType());
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
                        "against_electric, against_grass, against_ice, against_fighting, against_poison, against_ground, against_flying, " +
                        "against_psychic, against_bug, against_rock, against_ghost, against_dragon, against_dark, against_steel, against_fairy, img_icon," +
                        "img_large"), objects, PokemonDTO.class));
                total = ((BigInteger) countQuery.getSingleResult()).longValue();
            }

            List<Type> types = typeRepo.findAll();
            Map<String, TypeDTO> typeMap = types.stream()
                    .map(e -> typeMapper.toDto(e))
                    .collect(Collectors.toMap(TypeDTO::getCode, type -> type));

            List<Ability> abilities = abilityRepo.findAll();
            Map<Long, AbilityDTO> abilityMap = abilities.stream()
                    .map(e -> abilityMapper.toDto(e))
                    .collect(Collectors.toMap(AbilityDTO::getId, ability -> ability));

            rsDTOs = rsDTOs.stream()
                    .peek(e -> {
                        e.setImgIcon(DataUtils.loadImageAsBase64(imgPokemonIcon + e.getImgIcon()));
                        if (e.getType1() != null) {
                            e.setType1Entity(typeMap.get(e.getType1()));
                        }
                        if (e.getType2() != null) {
                            e.setType2Entity(typeMap.get(e.getType2()));
                        }
                        if (e.getAbility1() != null) {
                            e.setAbility1E(abilityMap.get(e.getAbility1()));
                        }
                        if (e.getAbility2() != null) {
                            e.setAbility2E(abilityMap.get(e.getAbility2()));
                        }
                        if (e.getAbilityHidden() != null) {
                            e.setAbilityHiddenE(abilityMap.get(e.getAbilityHidden()));
                        }
                    })
                    .collect(Collectors.toList());
            return new PageImpl<>(rsDTOs, pageable, total);
        } catch (Exception e) {
            log.error("Error in search PokemonDTO: {}", e.getMessage(), e);
            throw new BusinessException("Error in search PokemonDTO: " + e.getMessage());
        }
    }
}
