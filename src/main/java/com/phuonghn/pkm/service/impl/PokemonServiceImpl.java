package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.Constants;
import com.phuonghn.pkm.common.exeption.BusinessException;
import com.phuonghn.pkm.common.utils.DataUtils;
import com.phuonghn.pkm.entity.Evolution;
import com.phuonghn.pkm.entity.Pokemon;
import com.phuonghn.pkm.repository.*;
import com.phuonghn.pkm.service.PokemonService;
import com.phuonghn.pkm.service.dto.*;
import com.phuonghn.pkm.service.mapper.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final TypeRepo typeRepo;
    private final AbilityRepo abilityRepo;
    private final EvolutionRepo evolutionRepo;
    private final EvolutionConditionMapper evolutionConditionMapper;
    private final PokemonMoveMapper pokemonMoveMapper;
    private final PokemonMoveRepo pokemonMoveRepo;
    private final MoveRepo moveRepo;
    private final MoveMapper moveMapper;
    private final TypeMapper typeMapper;
    private final AbilityMapper abilityMapper;
    @Value("${image.pokemon-large}")
    private String imgPokemonLarge;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;
    @Value("${image.items}")
    private String imgItems;

    @Override
    public List<PokemonDTO> findAll(String generationCode, String type) {
        return pokemonRepo.findAllByGen(generationCode, type);
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
            Map<String, TypeDTO> typeMap = typeRepo.findAll().stream()
                    .map(typeMapper::toDto)
                    .collect(Collectors.toMap(TypeDTO::getCode, type -> type));
            Map<Long, AbilityDTO> abilityMap = abilityRepo.findAll().stream()
                    .map(abilityMapper::toDto)
                    .collect(Collectors.toMap(AbilityDTO::getId, ability -> ability));
            if (pokemonOptional.isPresent()) {
                PokemonDTO pokemonDTO = pokemonMapper.toDto(pokemonOptional.get());
                pokemonDTO.setImgLarge(DataUtils.loadImageAsBase64(imgPokemonLarge + pokemonDTO.getImgLarge()));

                if (pokemonDTO.getType1() != null) {
                    pokemonDTO.setType1Entity(typeMap.get(pokemonDTO.getType1()));
                }
                if (pokemonDTO.getType2() != null) {
                    pokemonDTO.setType2Entity(typeMap.get(pokemonDTO.getType2()));
                }

                if (pokemonDTO.getAbility1() != null) {
                    pokemonDTO.setAbility1E(abilityMap.get(pokemonDTO.getAbility1()));
                }
                if (pokemonDTO.getAbility2() != null) {
                    pokemonDTO.setAbility2E(abilityMap.get(pokemonDTO.getAbility2()));
                }
                if (pokemonDTO.getAbilityHidden() != null) {
                    pokemonDTO.setAbilityHiddenE(abilityMap.get(pokemonDTO.getAbilityHidden()));
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

                // set pokemon moves
                pokemonDTO.setPokemonMoves(getPokemonMove(id, typeMap));

                return pokemonDTO;
            }
            return null;
        } catch (Exception e) {
            e.getStackTrace();
            throw new BusinessException("Error", e);
        }
    }

    private List<PokemonMoveDTO> getPokemonMove(Long id, Map<String, TypeDTO> typeMap) {
        List<PokemonMoveDTO> rs = new ArrayList<>();
        try {
            List<PokemonMoveDTO> pokemonMoves = pokemonMoveMapper.toDto(pokemonMoveRepo.findByPokemonId(id));
            if (!DataUtils.isNullOrEmpty(pokemonMoves)) {
                List<Long> moveIds = pokemonMoves.stream()
                        .map(PokemonMoveDTO::getMoveId)
                        .distinct()
                        .collect(Collectors.toList());

                Map<Long, MoveDTO> moveMap = moveRepo.findAllById(moveIds).stream()
                        .map(moveMapper::toDto)
                        .collect(Collectors.toMap(MoveDTO::getId, dto -> dto));

                for (PokemonMoveDTO move : pokemonMoves) {
                    MoveDTO moveDTO = moveMap.get(move.getMoveId());
                    if (moveDTO != null) {
                        TypeDTO type = typeMap.get(moveDTO.getType());
                        if (type != null) {
                            moveDTO.setTypeBgColor(type.getBgHexColor());
                            moveDTO.setTypeTextColor(type.getTextHexColor());
                            moveDTO.setTypeName(type.getName());
                        } else {
                            log.warn("Type not found for move: {}", moveDTO.getName());
                        }
                        move.setMove(moveDTO);
                        rs.add(move);
                    }
                }
                return rs;
            }
        } catch (Exception e) {
            log.error("Error getting Pokemon moves for id {}: {}", id, e.getMessage());
        }
        return null;
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
                                item.setImageUrl(DataUtils.loadImageAsBase64(imgItems + item.getImage()));
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
                dto.setImgLarge(DataUtils.loadImageAsBase64(imgPokemonIcon + dto.getImgLarge()));

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
                                item.setImageUrl(DataUtils.loadImageAsBase64(imgItems + item.getImage()));
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
