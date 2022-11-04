package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.Pokemon;
import com.phuonghn.pkm.service.dto.PokemonDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PokemonMapper extends EntityMapper<PokemonDTO, Pokemon> {

}
