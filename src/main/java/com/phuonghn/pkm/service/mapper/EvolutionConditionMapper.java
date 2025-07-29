package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.EvolutionCondition;
import com.phuonghn.pkm.service.dto.EvolutionConditionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvolutionConditionMapper extends EntityMapper<EvolutionConditionDTO, EvolutionCondition> {

}
