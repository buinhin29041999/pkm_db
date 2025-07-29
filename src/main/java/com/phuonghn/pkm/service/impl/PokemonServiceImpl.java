package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.exeption.BusinessException;
import com.phuonghn.pkm.entity.Evolution;
import com.phuonghn.pkm.entity.Pokemon;
import com.phuonghn.pkm.repository.*;
import com.phuonghn.pkm.service.PokemonService;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import com.phuonghn.pkm.service.mapper.EvolutionConditionMapper;
import com.phuonghn.pkm.service.mapper.PokemonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokemonServiceImpl implements PokemonService {
    private final PokemonRepo pokemonRepo;
    private final PokemonMapper pokemonMapper;
    private final EvolutionConditionRepo evolutionConditionRepo;
    @Value("${image.pokemon-large}")
    private String imgPokemonLarge;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;
    private final TypeRepo typeRepo;
    private final AbilityRepo abilityRepo;
    private final EvolutionRepo evolutionRepo;
    private final EvolutionConditionMapper evolutionConditionMapper;

    @Override
    public List<PokemonDTO> findAll() {
        return pokemonMapper.toDto(pokemonRepo.findAll()
                .stream()
                .peek(e -> e.setImgLarge(imgPokemonIcon + File.separator + e.getImgIcon()))
                .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public Page<PokemonDTO> search(PokemonDTO pokemonDTO, Pageable pageable) {
        return pokemonRepo.search(pokemonDTO, pageable);
    }

    @Override
    public PokemonDTO detail(Long id) {
        try {
            Optional<Pokemon> pokemonOptional = pokemonRepo.findById(id);
            if (pokemonOptional.isPresent()) {
                PokemonDTO pokemonDTO = pokemonMapper.toDto(pokemonOptional.get());
                try {
                    ClassPathResource resource = new ClassPathResource(imgPokemonLarge + pokemonDTO.getImgLarge());
                    pokemonDTO.setImgLarge("data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(resource.getFile().toPath())));
                } catch (Exception e) {
                    log.error("Error reading image file: {}", e.getMessage());
                }

                if (pokemonDTO.getType1() != null) {
                    typeRepo.findByCode(pokemonDTO.getType1()).ifPresent(pokemonDTO::setType1Entity);
                }
                if (pokemonDTO.getType2() != null) {
                    typeRepo.findByCode(pokemonDTO.getType2()).ifPresent(pokemonDTO::setType2Entity);
                }

                if (pokemonDTO.getAbility1() != null) {
                    abilityRepo.findByName(pokemonDTO.getAbility1()).ifPresent(pokemonDTO::setAbility1E);
                }
                if (pokemonDTO.getAbility2() != null) {
                    abilityRepo.findByName(pokemonDTO.getAbility2()).ifPresent(pokemonDTO::setAbility2E);
                }
                if (pokemonDTO.getAbilityHidden() != null) {
                    abilityRepo.findByName(pokemonDTO.getAbilityHidden()).ifPresent(pokemonDTO::setAbilityHiddenE);
                }

                // Set evolution details
                List<Evolution> evolutions = evolutionRepo.findAll();
                List<Evolution> evolutionsFiltered = evolutions.stream()
                        .filter(e -> Objects.nonNull(e.getFromId()) && Objects.nonNull(e.getToId()))
                        .collect(Collectors.toList());

                List<PokemonDTO> evolutionChains = pokemonMapper.toDto(getEvolutionChain(id, evolutionsFiltered));
                PokemonDTO prev = null;
                for (PokemonDTO chain : evolutionChains) {
                    try {
                        ClassPathResource resource = new ClassPathResource(imgPokemonLarge + chain.getImgLarge());
                        chain.setImgLarge("data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(resource.getFile().toPath())));
                    } catch (Exception e) {
                        log.error("Error reading image file: {}", e.getMessage());
                    }
                    if (prev == null) {
                        prev = chain;
                        continue;
                    }

                    Long prevId = prev.getId();
                    Long currentId = chain.getId();
                    Evolution evolution = evolutionsFiltered.stream()
                            .filter(e -> e.getFromId().equals(prevId) && e.getToId().equals(currentId))
                            .findFirst()
                            .orElse(null);

                    if (evolution != null) {
                        chain.setConditionDTOS(evolutionConditionMapper.toDto(evolutionConditionRepo.findAllByEvolutionId(evolution.getId())));
                    }
                    prev = chain;

                }
                pokemonDTO.setEvolutionChains(evolutionChains);

                return pokemonDTO;
            }
            return null;
        } catch (Exception e) {
            e.getStackTrace();
            throw new BusinessException("Error", e);
        }

    }

    public List<Pokemon> getEvolutionChain(long inputId, List<Evolution> evolutionsFiltered) {

        // 1. Đồ thị tiến hóa thuận (from -> to) và nghịch (to -> from)
        Map<Long, Long> nextMap = new HashMap<>();
        Map<Long, Long> prevMap = new HashMap<>();

        for (Evolution pair : evolutionsFiltered) {
            Long from = pair.getFromId();
            Long to = pair.getToId();
            nextMap.put(from, to);
            prevMap.put(to, from);
        }

        // 2. Tìm gốc chuỗi tiến hóa (đi ngược về đầu)
        Long startId = inputId;
        while (prevMap.containsKey(startId)) {
            startId = prevMap.get(startId);
        }

        // 3. Duyệt tiến theo thứ tự tiến hóa
        List<Long> chain = new ArrayList<>();
        Long currentId = startId;
        chain.add(currentId);
        while (nextMap.containsKey(currentId)) {
            currentId = nextMap.get(currentId);
            chain.add(currentId);
        }

        // 4. Lấy thông tin Pokemon theo ID đã có
        List<Pokemon> pokemons = pokemonRepo.findAllById(chain);

        // 5. Sắp xếp theo đúng thứ tự chain
        Map<Long, Pokemon> idToPokemon = pokemons.stream()
                .collect(Collectors.toMap(Pokemon::getId, p -> p));

        List<Pokemon> ordered = new ArrayList<>();
        for (Long id : chain) {
            if (idToPokemon.containsKey(id)) {
                ordered.add(idToPokemon.get(id));
            }
        }

        return ordered;
    }
}
