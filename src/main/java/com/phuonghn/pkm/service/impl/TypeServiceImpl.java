package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.exeption.BadRequestException;
import com.phuonghn.pkm.common.utils.Translator;
import com.phuonghn.pkm.entity.Type;
import com.phuonghn.pkm.repository.TypeRepo;
import com.phuonghn.pkm.service.TypeService;
import com.phuonghn.pkm.service.dto.TypeDTO;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;
import com.phuonghn.pkm.service.mapper.TypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<TypeDTO> getTypeAttack(TypeDetailDTO typeDetailDTO) {
        if ((typeDetailDTO.getAtkTypeId() != null && typeDetailDTO.getDefTypeId() == null)
                || (typeDetailDTO.getAtkTypeId() == null && typeDetailDTO.getDefTypeId() != null)) {
            Long typeId = typeDetailDTO.getAtkTypeId() != null ? typeDetailDTO.getAtkTypeId() : typeDetailDTO.getDefTypeId();
            return typeRepo.getTypeAtk(typeId);
        }
        return null;
    }

    @Override
    public List<TypeDTO> getTypeDefense(TypeDetailDTO typeDetailDTO) {
        List<Long> ids = new ArrayList<>();
        if ((typeDetailDTO.getAtkTypeId() != null && typeDetailDTO.getDefTypeId() == null)
                || (typeDetailDTO.getAtkTypeId() == null && typeDetailDTO.getDefTypeId() != null)) {
            ids.add(typeDetailDTO.getAtkTypeId() != null ? typeDetailDTO.getAtkTypeId() : typeDetailDTO.getDefTypeId());
            return typeRepo.getTypeDef(ids);

        } else if (typeDetailDTO.getAtkTypeId() != null && typeDetailDTO.getDefTypeId() != null) {
            ids.add(typeDetailDTO.getAtkTypeId());
            ids.add(typeDetailDTO.getDefTypeId());
            List<TypeDTO> res = typeRepo.getTypeDef(ids);
            List<TypeDTO> finalRes = new ArrayList<>();
            while (!res.isEmpty()) {
                List<TypeDTO> tmp = res.stream().filter(e -> Objects.equals(e.getId(), res.get(0).getId())).collect(Collectors.toList());
                TypeDTO typeAdd = tmp.get(0);
                typeAdd.setEffect(tmp.get(0).getEffect() * tmp.get(1).getEffect());
                finalRes.add(typeAdd);
                res.removeAll(tmp);
            }
            return finalRes;
        }
        return null;
    }
}
