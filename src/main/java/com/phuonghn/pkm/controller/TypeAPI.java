package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.TypeService;
import com.phuonghn.pkm.service.dto.TypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/type")
@RequiredArgsConstructor
public class TypeAPI {
    final TypeService typeService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(typeService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TypeDTO typeDTO) {
        return new ResponseEntity<>(typeService.create(typeDTO), HttpStatus.OK);
    }

}
