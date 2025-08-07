package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.MoveService;
import com.phuonghn.pkm.service.dto.MoveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/move")
@RequiredArgsConstructor
public class MoveController {
    private final MoveService moveService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(moveService.findAll(), HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody MoveDTO dto, Pageable pageable) {
        return new ResponseEntity<>(moveService.search(dto, pageable), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(moveService.detail(id), HttpStatus.OK);
    }

}
