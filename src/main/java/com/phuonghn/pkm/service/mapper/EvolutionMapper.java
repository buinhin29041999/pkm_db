package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.Evolution;
import com.phuonghn.pkm.service.dto.EvolutionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EvolutionMapper extends EntityMapper<EvolutionDTO, Evolution> {

}
