package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.Constants;
import com.phuonghn.pkm.common.exeption.BusinessException;
import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.entity.Evolution;
import com.phuonghn.pkm.entity.Pokemon;
import com.phuonghn.pkm.repository.*;
import com.phuonghn.pkm.service.PokemonService;
import com.phuonghn.pkm.service.dto.EvolutionChainDTO;
import com.phuonghn.pkm.service.dto.EvolutionConditionDTO;
import com.phuonghn.pkm.service.dto.ItemDTO;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import com.phuonghn.pkm.service.mapper.EvolutionConditionMapper;
import com.phuonghn.pkm.service.mapper.ItemMapper;
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
    private final ItemRepo itemRepo;
    private final ItemMapper itemMapper;
    @Value("${image.pokemon-large}")
    private String imgPokemonLarge;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;
    @Value("${image.items}")
    private String imgItems;
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
    @Transactional(readOnly = true)
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

                List<EvolutionConditionDTO> evolutionConditions = evolutionConditionMapper.toDto(evolutionConditionRepo.findAll());
                Map<Long, List<EvolutionConditionDTO>> mapConditions = evolutionConditions.stream()
                        .collect(Collectors.groupingBy(EvolutionConditionDTO::getEvolutionId));

                List<ItemDTO> items = itemMapper.toDto(itemRepo.findAll());
                Map<String, ItemDTO> mapItem = items.stream()
                        .collect(Collectors.toMap(ItemDTO::getCode, item -> item));

                pokemonDTO.setEvolutionChains(getEvolutionChain(id, evolutions, mapItem));

                // set special evolution
                pokemonDTO.setSpecialForm(getSpecialForm(id, evolutions, mapConditions, mapItem));

                return pokemonDTO;
            }
            return null;
        } catch (Exception e) {
            e.getStackTrace();
            throw new BusinessException("Error", e);
        }
    }

    private List<EvolutionChainDTO> getSpecialForm(Long id, List<Evolution> evolutions, Map<Long, List<EvolutionConditionDTO>> mapConditions, Map<String, ItemDTO> mapItem) {

        try {
            List<EvolutionChainDTO> chainDTOS = new ArrayList<>();
            List<Evolution> evolutionsFiltered = evolutions.stream()
                    .filter(e -> Objects.nonNull(e.getFromId()) && Objects.nonNull(e.getToId()))
                    .filter(e -> !Constants.EVOLUTION_TYPE.NORMAL.equals(e.getType()))
                    .filter(e -> id.equals(e.getFromId()))
                    .collect(Collectors.toList());

            if (!DataUtils.isNullOrEmpty(evolutionsFiltered)) {
                List<PokemonDTO> pokemonDTOS = pokemonMapper.toDto(pokemonRepo.findAllById(evolutionsFiltered.stream().map(Evolution::getToId)
                        .collect(Collectors.toList())));
                Map<Long, PokemonDTO> pokemonMap = pokemonDTOS.stream().collect(Collectors.toMap(PokemonDTO::getId, p -> p));
                for (Evolution evolution : evolutionsFiltered) {
                    EvolutionChainDTO dto = new EvolutionChainDTO();
                    dto.setSpecialForm(evolution.getType());
                    dto.setPokemon(pokemonMap.get(evolution.getToId()));

                    List<EvolutionConditionDTO> conditions = mapConditions.get(evolution.getId());
                    if (!DataUtils.isNullOrEmpty(conditions)) {
                        for (EvolutionConditionDTO condition : conditions) {
                            if (condition.getItemCode() != null) {
                                ItemDTO item = mapItem.get(condition.getItemCode());
                                item.setImageUrl(loadImageAsBase64(imgItems + item.getImage()));
                                if (item != null) {
                                    condition.setItem(item);
                                }
                            }
                        }
                        dto.setConditions(conditions);
                    }
                    chainDTOS.add(dto);
                }
            }

            return chainDTOS;
        } catch (Exception e) {
            log.error("Error getting special evolution chain for id {}: {}", id, e.getMessage());
            return Collections.emptyList();
        }

    }

    private List<EvolutionChainDTO> getEvolutionChain(Long id, List<Evolution> evolutions, Map<String, ItemDTO> mapItem) {

        try {

            List<Evolution> evolutionsFiltered = evolutions.stream()
                    .filter(e -> Objects.nonNull(e.getFromId()) && Objects.nonNull(e.getToId()))
                    .filter(e -> Constants.EVOLUTION_TYPE.NORMAL.equals(e.getType()))
                    .collect(Collectors.toList());

            List<EvolutionChainDTO> chainDTOS = getEvolutionChain(id, evolutionsFiltered);

            for (EvolutionChainDTO chain : chainDTOS) {
                PokemonDTO dto = chain.getPokemon();
                dto.setImgLarge(loadImageAsBase64(imgPokemonLarge + dto.getImgLarge()));

                // Set điều kiện tiến hóa
                if (chain.getParentId() != null) {
                    Evolution evolution = evolutionsFiltered.stream()
                            .filter(e -> e.getFromId().equals(chain.getParentId()) && e.getToId().equals(dto.getId()))
                            .findFirst()
                            .orElse(null);

                    if (evolution != null) {
                        List<EvolutionConditionDTO> conditions = evolutionConditionMapper.toDto(evolutionConditionRepo.findAllByEvolutionId(evolution.getId()));
                        for (EvolutionConditionDTO condition : conditions) {
                            if (condition.getItemCode() != null) {
                                ItemDTO item = mapItem.get(condition.getItemCode());
                                item.setImageUrl(loadImageAsBase64(imgItems + item.getImage()));
                                if (item != null) {
                                    condition.setItem(item);
                                }
                            }
                        }
                        chain.setConditions(conditions);
                    }
                }
            }
            return chainDTOS;
        } catch (Exception e) {
            log.error("Error getting evolution chain for id {}: {}", id, e.getMessage());
            return Collections.emptyList();
        }
    }

    private String loadImageAsBase64(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(resource.getFile().toPath()));
        } catch (Exception e) {
            log.warn("Image not found: {}", path);
            return null;
        }
    }

    public List<EvolutionChainDTO> getEvolutionChain(long inputId, List<Evolution> evolutionsFiltered) {

        // Đồ thị tiến hóa đa nhánh
        Map<Long, List<Long>> nextMap = new HashMap<>();
        Map<Long, List<Long>> prevMap = new HashMap<>();

        for (Evolution pair : evolutionsFiltered) {
            Long from = pair.getFromId();
            Long to = pair.getToId();

            nextMap.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            prevMap.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        // Tìm gốc chuỗi tiến hóa
        Long startId = inputId;
        while (prevMap.containsKey(startId)) {
            startId = prevMap.get(startId).get(0);
        }

        // Duyệt DFS và lưu parent-child
        Map<Long, Long> childToParent = new LinkedHashMap<>();
        dfsWithParent(startId, null, nextMap, childToParent);

        // Lấy toàn bộ ID cần thiết
        List<Long> allIds = new ArrayList<>(childToParent.keySet());
        List<Pokemon> pokemons = pokemonRepo.findAllById(allIds);

        // Map id -> Pokemon
        Map<Long, Pokemon> idToPokemon = pokemons.stream()
                .collect(Collectors.toMap(Pokemon::getId, p -> p));

        // Tạo danh sách kết quả
        List<EvolutionChainDTO> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : childToParent.entrySet()) {
            Long id = entry.getKey();
            Long parentId = entry.getValue();
            if (idToPokemon.containsKey(id)) {
                result.add(new EvolutionChainDTO(parentId, pokemonMapper.toDto(idToPokemon.get(id))));
            }
        }

        return result;
    }

    private void dfsWithParent(Long current, Long parentId, Map<Long, List<Long>> graph, Map<Long, Long> childToParent) {
        if (childToParent.containsKey(current)) return;
        childToParent.put(current, parentId);
        for (Long next : graph.getOrDefault(current, Collections.emptyList())) {
            dfsWithParent(next, current, graph, childToParent);
        }
    }
}
