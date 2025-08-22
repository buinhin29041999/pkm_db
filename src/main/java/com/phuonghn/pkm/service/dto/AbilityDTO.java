package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AbilityDTO {
    private Long id;
    private String name;
    private String description;
    private String descriptionVn;
    private String generation;
}
