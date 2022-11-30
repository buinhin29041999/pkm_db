package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.repository.PokemonRepo;
import com.phuonghn.pkm.service.PokemonService;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import com.phuonghn.pkm.service.mapper.PokemonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PokemonServiceImpl implements PokemonService {

    private final PokemonRepo pokemonRepo;

    private final PokemonMapper pokemonMapper;

    @Override
    public List<PokemonDTO> findAll() {
        return pokemonMapper.toDto(pokemonRepo.findAll());
    }

    @Override
    public Page<PokemonDTO> search(PokemonDTO pokemonDTO, Pageable pageable) {
        return pokemonRepo.search(pokemonDTO, pageable);
    }
}
