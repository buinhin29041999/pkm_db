package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;

import java.util.List;

public interface TypeService {
    List<TypeDTO> findAll();

    TypeDTO create(TypeDTO typeDTO);

    List<TypeDTO> getTypeAttack(TypeDetailDTO typeDetailDTO);

    List<TypeDTO> getTypeDefense(TypeDetailDTO typeDetailDTO);
}
