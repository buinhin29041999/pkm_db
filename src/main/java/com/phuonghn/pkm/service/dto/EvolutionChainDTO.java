package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvolutionChainDTO {
    private Long parentId;
    private PokemonDTO pokemon;
    private String specialForm;
    private List<EvolutionConditionDTO> conditions;

    public EvolutionChainDTO(Long parentId, PokemonDTO pokemon) {
        this.pokemon = pokemon;
        this.parentId = parentId;
    }
}

