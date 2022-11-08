package com.phuonghn.pkm.repository;

import com.phuonghn.pkm.service.dto.TypeDTO;

import java.util.List;

public interface TypeRepoCustom {
    List<TypeDTO> getTypeAtk(Long typeId);

    List<TypeDTO> getTypeDef(List<Long> typeIds);
}
