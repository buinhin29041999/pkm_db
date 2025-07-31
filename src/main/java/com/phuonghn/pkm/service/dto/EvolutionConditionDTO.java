package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvolutionConditionDTO {
    private Long id;
    private String type;
    private String value;
    private String itemCode;
    private Long evolutionId;
    private ItemDTO item;
}

