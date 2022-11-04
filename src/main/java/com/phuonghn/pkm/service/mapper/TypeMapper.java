package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.Type;
import com.phuonghn.pkm.service.dto.TypeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeMapper extends EntityMapper<TypeDTO, Type> {

}
