package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.TypeDetailService;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/type-detail")
@RequiredArgsConstructor
public class TypeDetailAPI {
    final TypeDetailService typeDetailService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TypeDetailDTO typeDetailDTO) {
        return ResponseEntity.ok(typeDetailService.create(typeDetailDTO));
    }

}
