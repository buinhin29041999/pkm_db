package com.phuonghn.pkm.service;

import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    List<TypeDTO> findAll();

    Page<TypeDTO> search(TypeDTO dto, Pageable pageable);

    TypeDTO create(TypeDTO typeDTO);

    List<TypeDTO> getTypeAttack(TypeDetailDTO typeDetailDTO);

    List<TypeDTO> getTypeDefense(TypeDetailDTO typeDetailDTO);
}
