package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.PokemonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PokemonService {

    List<PokemonDTO> findAll();

    Page<PokemonDTO> search(PokemonDTO pokemonDTO, Pageable pageable);

    PokemonDTO detail(Long id);
}
