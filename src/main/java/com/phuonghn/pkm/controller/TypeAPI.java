package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.TypeDetailService;
import com.phuonghn.pkm.service.TypeService;
import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/type")
@RequiredArgsConstructor
public class TypeAPI {
    private final TypeService typeService;
    private final TypeDetailService typeDetailService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(typeService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TypeDTO typeDTO) {
        return new ResponseEntity<>(typeService.create(typeDTO), HttpStatus.OK);
    }

    @PostMapping("type-atk")
    public ResponseEntity<?> getTypeAttack(@RequestBody TypeDetailDTO typeDetailDTO) {
        return new ResponseEntity<>(typeService.getTypeAttack(typeDetailDTO), HttpStatus.OK);
    }

    @PostMapping("type-def")
    public ResponseEntity<?> getTypeDefense(@RequestBody TypeDetailDTO typeDetailDTO) {
        return new ResponseEntity<>(typeService.getTypeDefense(typeDetailDTO), HttpStatus.OK);
    }

}
