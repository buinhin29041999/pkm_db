package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.exeption.BadRequestException;
import com.phuonghn.pkm.common.utils.Translator;
import com.phuonghn.pkm.entity.Type;
import com.phuonghn.pkm.repository.TypeRepo;
import com.phuonghn.pkm.service.TypeService;
import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.mapper.TypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {
    final TypeRepo typeRepo;
    final TypeMapper typeMapper;

    @Override
    public List<TypeDTO> findAll() {
        return typeMapper.toDto(typeRepo.findAll());
    }

    @Override
    public TypeDTO create(TypeDTO typeDTO) {
        if (typeDTO.getId() != null) {
            throw new BadRequestException(Translator.toLocale("validate.01"));
        }
        typeRepo.findByName(typeDTO.getName()).ifPresent(type -> {
            throw new BadRequestException("Type is already defined");
        });
        Type type = typeRepo.save(typeMapper.toEntity(typeDTO));
        return typeMapper.toDto(type);
    }
}
