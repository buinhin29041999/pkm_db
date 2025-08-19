package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonMoveDTO {
    private Long id;
    private Long pokemonId;
    private Long moveId;
    private String learnMethod;
    private Long level;
    private MoveDTO move;
}
