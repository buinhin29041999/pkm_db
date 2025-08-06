package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.Move;
import com.phuonghn.pkm.service.dto.MoveDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoveMapper extends EntityMapper<MoveDTO, Move> {

}
