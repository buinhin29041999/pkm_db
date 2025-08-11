package com.phuonghn.pkm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PokemonMove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "pokemon_id")
    private Long pokemonId;
    @Basic
    @Column(name = "pokedex_number")
    private String pokedexNumber;
    @Basic
    @Column(name = "pokemon_name")
    private String pokemonName;
    @Basic
    @Column(name = "move_id")
    private Long moveId;
    @Basic
    @Column(name = "learn_method")
    private String learnMethod;
    @Basic
    @Column(name = "level")
    private Long level;
}
