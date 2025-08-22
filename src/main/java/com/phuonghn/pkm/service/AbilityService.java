package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.AbilityDTO;
import com.phuonghn.pkm.service.dto.PokemonAbilityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AbilityService {

    List<AbilityDTO> findAll();

    Page<AbilityDTO> search(AbilityDTO dto, Pageable pageable);

    AbilityDTO detail(Long id);

    PokemonAbilityDTO loadPokemonWithAbility(Long id);
}
