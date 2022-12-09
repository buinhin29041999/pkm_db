package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.PokemonService;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pokemon")
@RequiredArgsConstructor
public class PokemonAPI {

    private final PokemonService pokemonService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(pokemonService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(pokemonService.detail(id), HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody PokemonDTO pokemonDTO, Pageable pageable) {
        return new ResponseEntity<>(pokemonService.search(pokemonDTO, pageable), HttpStatus.OK);
    }

}
