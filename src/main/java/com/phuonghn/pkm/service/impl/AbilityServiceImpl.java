package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.exeption.BusinessException;
import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.entity.Ability;
import com.phuonghn.pkm.repository.AbilityRepo;
import com.phuonghn.pkm.repository.PokemonRepo;
import com.phuonghn.pkm.repository.TypeRepo;
import com.phuonghn.pkm.service.AbilityService;
import com.phuonghn.pkm.service.dto.AbilityDTO;
import com.phuonghn.pkm.service.dto.PokemonAbilityDTO;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.mapper.AbilityMapper;
import com.phuonghn.pkm.service.mapper.TypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AbilityServiceImpl implements AbilityService {

    private final AbilityMapper abilityMapper;
    private final AbilityRepo abilityRepo;
    private final PokemonRepo pokemonRepo;
    private final TypeRepo typeRepo;
    private final TypeMapper typeMapper;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;

    @Override
    public List<AbilityDTO> findAll() {
        return Collections.emptyList();
    }

    @Override
    public Page<AbilityDTO> search(AbilityDTO dto, Pageable pageable) {
        return abilityRepo.search(dto.getName(), dto.getGeneration(), pageable);
    }

    @Override
    public AbilityDTO detail(Long id) {
        Ability ability = abilityRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ability not found with id: " + id));
        return abilityMapper.toDto(ability);
    }

    @Override
    public PokemonAbilityDTO loadPokemonWithAbility(Long id) {
        try {
            PokemonAbilityDTO dto = new PokemonAbilityDTO();

            // Load type map sẵn
            Map<String, TypeDTO> typeMap = typeRepo.findAll().stream()
                    .map(typeMapper::toDto)
                    .collect(Collectors.toMap(TypeDTO::getCode, type -> type));

            // Cache ảnh để tránh đọc nhiều lần
            Map<String, String> imageCache = new HashMap<>();

            // Hàm xử lý chung cho 1 list
            Function<List<PokemonDTO>, List<PokemonDTO>> enrich = list -> list.stream()
                    .peek(pkm -> enrichPokemon(pkm, typeMap, imageCache))
                    .collect(Collectors.toList());

            dto.setPkmNormalAbilities(enrich.apply(pokemonRepo.findPkmWithNormalAbility(id)));
            dto.setPkmHiddenAbilities(enrich.apply(pokemonRepo.findPkmWithHiddenAbility(id)));

            return dto;
        } catch (Exception e) {
            throw new BusinessException("Error", e);
        }
    }

    private void enrichPokemon(PokemonDTO pkm, Map<String, TypeDTO> typeMap, Map<String, String> imageCache) {
        // Map type
        pkm.setType1Entity(typeMap.get(pkm.getType1()));
        if (pkm.getType2() != null) {
            pkm.setType2Entity(typeMap.get(pkm.getType2()));
        }

        // Map ảnh (có cache)
        try {
            String imgFile = pkm.getImgIcon();
            String base64 = imageCache.computeIfAbsent(imgFile, file -> DataUtils.loadImageAsBase64(imgPokemonIcon + file));
            if (base64 != null) {
                pkm.setImgIcon(base64);
            }
        } catch (Exception ex) {
            log.error("Unexpected error when processing image: {}", pkm.getImgIcon(), ex);
        }
    }

}
