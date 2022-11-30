package com.phuonghn.pkm.service.dto;

import lombok.Data;

@Data
public class PokemonDTO {

    private Long id;
    private Integer pokedexNumber;
    private String name;
    private String germanName;
    private String japaneseName;
    private Integer generation;
    private String status;
    private String species;
    private Integer typeNumber;
    private Long type1;
    private Long type2;
    private Double heightM;
    private Double weightKg;
    private Integer abilitiesNumber;
    private Long ability1;
    private Long ability2;
    private Long abilityHidden;
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
    private Double againstFight;
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
}
