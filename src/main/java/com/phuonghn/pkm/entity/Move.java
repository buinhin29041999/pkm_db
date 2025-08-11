package com.phuonghn.pkm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "accuracy")
    private Double accuracy;
    @Basic
    @Column(name = "pp")
    private Long pp;
    @Basic
    @Column(name = "power")
    private Long power;
    @Basic
    @Column(name = "priority")
    private Long priority;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "generation")
    private String generation;
    @Basic
    @Column(name = "short_description")
    private String shortDescription;
    @Basic
    @Column(name = "damage_class")
    private String damageClass;
    @Basic
    @Column(name = "make_contact")
    private Long makeContact;
    @Basic
    @Column(name = "z_move_effect")
    private String zMoveEffect;
}
