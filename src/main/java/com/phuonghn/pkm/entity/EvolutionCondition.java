package com.phuonghn.pkm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class EvolutionCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "value")
    private String value;
    @Basic
    @Column(name = "item_code")
    private String itemCode;
    @Basic
    @Column(name = "evolution_id")
    private Long evolutionId;
}
