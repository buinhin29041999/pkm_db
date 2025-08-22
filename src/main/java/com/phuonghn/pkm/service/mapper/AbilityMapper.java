package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.Ability;
import com.phuonghn.pkm.service.dto.AbilityDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AbilityMapper extends EntityMapper<AbilityDTO, Ability> {

}
