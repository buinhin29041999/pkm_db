package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonDTO implements Serializable {

    List<PokemonMoveDTO> pokemonMoves;
    private Long id;
    private String pokedexNumber;
    private String name;
    private String germanName;
    private String japaneseName;
    private String generation;
    private String status;
    private String species;
    private Integer typeNumber;
    private String type1;
    private TypeDTO type1Entity;
    private String type2;
    private TypeDTO type2Entity;
    private Double heightM;
    private Double weightKg;
    private Integer abilitiesNumber;
    private Long ability1;
    private Long ability2;
    private Long abilityHidden;
    private AbilityDTO ability1E;
    private AbilityDTO ability2E;
    private AbilityDTO abilityHiddenE;
    private Integer totalPoints;
    private Integer hp;
    private Integer attack;
    private Integer defense;
    private Integer spAttack;
    private Integer spDefense;
    private Integer speed;
    private Integer catchRate;
    private Integer baseFriendship;
    private Integer baseExperience;
    private String growthRate;
    private Integer eggTypeNumber;
    private String eggType1;
    private String eggType2;
    private Double percentageMale;
    private Integer eggCycles;
    private Double againstNormal;
    private Double againstFire;
    private Double againstWater;
    private Double againstElectric;
    private Double againstGrass;
    private Double againstIce;
    private Double againstFighting;
    private Double againstPoison;
    private Double againstGround;
    private Double againstFlying;
    private Double againstPsychic;
    private Double againstBug;
    private Double againstRock;
    private Double againstGhost;
    private Double againstDragon;
    private Double againstDark;
    private Double againstSteel;
    private Double againstFairy;
    private String imgLarge;
    private String imgIcon;
    private String description;
    private List<EvolutionChainDTO> evolutionChains;
    private List<EvolutionChainDTO> specialForm;
    // Additional fields for search
    private String type;

    public PokemonDTO(Long id, String pokedexNumber, String name) {
        this.id = id;
        this.pokedexNumber = pokedexNumber;
        this.name = name;
    }

    public PokemonDTO(Long id, String pokedexNumber, String name, String imgIcon, String type1, String type2) {
        this.id = id;
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.imgIcon = imgIcon;
        this.type1 = type1;
        this.type2 = type2;
    }
}
