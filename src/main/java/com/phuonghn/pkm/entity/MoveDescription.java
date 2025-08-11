package com.phuonghn.pkm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MoveDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "move_id")
    private Long moveId;
    @Basic
    @Column(name = "generation")
    private String generation;
    @Basic
    @Column(name = "description")
    private String description;
}
