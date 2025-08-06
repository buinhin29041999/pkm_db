package com.phuonghn.pkm.controller;

import com.phuonghn.pkm.service.GlobalParamService;
import com.phuonghn.pkm.service.dto.GlobalParamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/global-param")
@RequiredArgsConstructor
public class GlobalParamController {
    private final GlobalParamService globalParamService;

    @PostMapping("search")
    public ResponseEntity<?> search(@RequestBody GlobalParamDTO dto) {
        return new ResponseEntity<>(globalParamService.search(dto), HttpStatus.OK);
    }

}
