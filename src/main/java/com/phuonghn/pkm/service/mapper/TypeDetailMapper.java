package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.TypeDetail;
import com.phuonghn.pkm.service.dto.TypeDetailDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeDetailMapper extends EntityMapper<TypeDetailDTO, TypeDetail> {

}
