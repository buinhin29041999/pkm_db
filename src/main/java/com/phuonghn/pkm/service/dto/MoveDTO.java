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
    private String typeBgColor;
    private String typeTextColor;
    private String generation;
    private String generationName;
    private String shortDescription;
    private String damageClass;
    private String damageClassName;
    public MoveDTO(Long id, String name, Double accuracy, Long pp, Long power, Long priority, String typeName, String type, String generation, String generationName, String damageClass, String shortDescription, String damageClassName) {
        this.id = id;
        this.name = name;
        this.accuracy = accuracy;
        this.pp = pp;
        this.power = power;
        this.priority = priority;
        this.typeName = typeName;
        this.type = type;
        this.generation = generation;
        this.generationName = generationName;
        this.damageClass = damageClass;
        this.shortDescription = shortDescription;
        this.damageClassName = damageClassName;
    }
}
