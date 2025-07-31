package com.phuonghn.pkm.service.mapper;

import com.phuonghn.pkm.entity.Item;
import com.phuonghn.pkm.service.dto.ItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {

}
