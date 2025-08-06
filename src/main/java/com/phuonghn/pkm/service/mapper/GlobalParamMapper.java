package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.GlobalParam;
import com.phuonghn.pkm.service.dto.GlobalParamDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GlobalParamMapper extends EntityMapper<GlobalParamDTO, GlobalParam> {

}
