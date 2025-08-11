package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.common.Constants;
import com.phuonghn.pkm.config.kafka.MessageProducer;
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
public class PokemonController {

    private final PokemonService pokemonService;
    private final MessageProducer messageProducer;

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "generationCode", required = false) String generationCode,
                                     @RequestParam(value = "type", required = false) String type) {
        return new ResponseEntity<>(pokemonService.findAll(generationCode, type), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(pokemonService.detail(id), HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody PokemonDTO pokemonDTO, Pageable pageable) {
        messageProducer.sendMessage(Constants.KAFKA_TOPIC.PKM_SEARCH, pokemonDTO.getName());
        return new ResponseEntity<>(pokemonService.search(pokemonDTO, pageable), HttpStatus.OK);
    }

}
