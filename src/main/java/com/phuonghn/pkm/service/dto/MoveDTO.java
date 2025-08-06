package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveDTO {

    private Long id;
    private String name;
    private Double accuracy;
    private Long pp;
    private Long power;
    private Long priority;
    private String type;
    private String typeName;
    private String generation;
    private String generationName;
    private String shortDescription;
    private String damageClass;
    private String damageClassName;
}
