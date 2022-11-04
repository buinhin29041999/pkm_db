package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.TypeDTO;

import java.util.List;

public interface TypeService {
    List<TypeDTO> findAll();

    TypeDTO create(TypeDTO typeDTO);
}
