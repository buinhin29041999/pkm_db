package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.service.dto.PokemonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PokemonRepoCustom {

    Page<PokemonDTO> search(PokemonDTO pokemonDTO, Pageable pageable);
}
