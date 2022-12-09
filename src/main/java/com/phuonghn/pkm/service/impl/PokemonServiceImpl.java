package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.exeption.BusinessException;
import com.phuonghn.pkm.entity.Pokemon;
import com.phuonghn.pkm.repository.PokemonRepo;
import com.phuonghn.pkm.service.PokemonService;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import com.phuonghn.pkm.service.mapper.PokemonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PokemonServiceImpl implements PokemonService {
    private final PokemonRepo pokemonRepo;
    private final PokemonMapper pokemonMapper;
    @Value("${image.pokemon-large}")
    private String imgPokemonLarge;
    @Value("${image.pokemon-icon}")
    private String imgPokemonIcon;

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
                pokemonDTO.setImgLarge("data:image/png;base64," + Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(imgPokemonLarge + "/" + pokemonDTO.getImgLarge()))));
                return pokemonDTO;
            }
            return null;
        } catch (Exception e) {
            throw new BusinessException("Error");
        }

    }
}
