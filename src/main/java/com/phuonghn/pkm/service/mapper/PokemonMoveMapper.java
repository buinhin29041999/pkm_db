package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.PokemonMove;
import com.phuonghn.pkm.service.dto.PokemonMoveDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PokemonMoveMapper extends EntityMapper<PokemonMoveDTO, PokemonMove> {

}
