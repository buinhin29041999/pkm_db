package com.phuonghn.pkm.service.impl;

import com.phuonghn.pkm.common.exeption.BadRequestException;
import com.phuonghn.pkm.common.utils.Translator;
import com.phuonghn.pkm.entity.TypeEffect;
import com.phuonghn.pkm.repository.TypeDetailRepo;
import com.phuonghn.pkm.service.TypeDetailService;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;
import com.phuonghn.pkm.service.mapper.TypeDetailMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TypeDetailServiceImpl implements TypeDetailService {
    private final TypeDetailRepo typeDetailRepo;
    private final TypeDetailMapper typeDetailMapper;

    @Override
    public TypeDetailDTO create(TypeDetailDTO typeDetailDTO) {
        if (typeDetailDTO.getId() != null) {
            throw new BadRequestException(Translator.toLocale("validate.01"));
        }
        typeDetailRepo.findByAtkTypeIdAndDefTypeId(typeDetailDTO.getAtkTypeId(), typeDetailDTO.getDefTypeId()).ifPresent(type -> {
            throw new BadRequestException("Type detail is already defined");
        });
        TypeEffect typeEffect = typeDetailRepo.save(typeDetailMapper.toEntity(typeDetailDTO));
        return typeDetailMapper.toDto(typeEffect);
    }


}
