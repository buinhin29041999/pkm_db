package com.phuonghn.pkm.service.dto;

import com.phuonghn.pkm.entity.Type;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PokemonDTO {
    private Long id;
    private String code;
    private String name;
    private Set<Type> roles = new HashSet<>();

}
