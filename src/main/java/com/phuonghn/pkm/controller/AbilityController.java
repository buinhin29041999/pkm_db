package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.AbilityService;
import com.phuonghn.pkm.service.dto.AbilityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/ability")
@RequiredArgsConstructor
public class AbilityController {
    private final AbilityService abilityService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(abilityService.findAll(), HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody AbilityDTO dto, Pageable pageable) {
        return new ResponseEntity<>(abilityService.search(dto, pageable), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(abilityService.detail(id), HttpStatus.OK);
    }

    @GetMapping("load-pokemon-with-ability/{id}")
    public ResponseEntity<?> loadPokemonWithAbility(@PathVariable("id") Long id) {
        return new ResponseEntity<>(abilityService.loadPokemonWithAbility(id), HttpStatus.OK);
    }
}
