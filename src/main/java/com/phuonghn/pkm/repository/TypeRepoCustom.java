package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.service.dto.TypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeRepoCustom {
    List<TypeDTO> getTypeAtk(Long typeId);

    List<TypeDTO> getTypeDef(List<Long> typeIds);

    Page<TypeDTO> search(TypeDTO dto, Pageable pageable);
}
