package com.phuonghn.pkm.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "pokedex_number", nullable = true)
    private Integer pokedexNumber;
    @Basic
    @Column(name = "name", nullable = true, length = 255)
    private String name;
    @Basic
    @Column(name = "german_name", nullable = true, length = 255)
    private String germanName;
    @Basic
    @Column(name = "japanese_name", nullable = true, length = 255)
    private String japaneseName;
    @Basic
    @Column(name = "generation", nullable = true)
    private Integer generation;
    @Basic
    @Column(name = "status", nullable = true, length = 255)
    private String status;
    @Basic
    @Column(name = "species", nullable = true, length = 255)
    private String species;
    @Basic
    @Column(name = "type_number", nullable = true)
    private Integer typeNumber;
    @Basic
    @Column(name = "type_1", nullable = true)
    private Long type1;
    @Basic
    @Column(name = "type_2", nullable = true)
    private Long type2;
    @Basic
    @Column(name = "height_m", nullable = true, precision = 0)
    private Double heightM;
    @Basic
    @Column(name = "weight_kg", nullable = true, precision = 0)
    private Double weightKg;
    @Basic
    @Column(name = "abilities_number", nullable = true)
    private Integer abilitiesNumber;
    @Basic
    @Column(name = "ability_1", nullable = true)
    private Long ability1;
    @Basic
    @Column(name = "ability_2", nullable = true)
    private Long ability2;
    @Basic
    @Column(name = "ability_hidden", nullable = true)
    private Long abilityHidden;
    @Basic
    @Column(name = "total_points", nullable = true)
    private Integer totalPoints;
    @Basic
    @Column(name = "hp", nullable = true)
    private Integer hp;
    @Basic
    @Column(name = "attack", nullable = true)
    private Integer attack;
    @Basic
    @Column(name = "defense", nullable = true)
    private Integer defense;
    @Basic
    @Column(name = "sp_attack", nullable = true)
    private Integer spAttack;
    @Basic
    @Column(name = "sp_defense", nullable = true)
    private Integer spDefense;
    @Basic
    @Column(name = "speed", nullable = true)
    private Integer speed;
    @Basic
    @Column(name = "catch_rate", nullable = true)
    private Integer catchRate;
    @Basic
    @Column(name = "base_friendship", nullable = true)
    private Integer baseFriendship;
    @Basic
    @Column(name = "base_experience", nullable = true)
    private Integer baseExperience;
    @Basic
    @Column(name = "growth_rate", nullable = true, length = 255)
    private String growthRate;
    @Basic
    @Column(name = "egg_type_number", nullable = true)
    private Integer eggTypeNumber;
    @Basic
    @Column(name = "egg_type_1", nullable = true, length = 255)
    private String eggType1;
    @Basic
    @Column(name = "egg_type_2", nullable = true, length = 255)
    private String eggType2;
    @Basic
    @Column(name = "percentage_male", nullable = true, precision = 0)
    private Double percentageMale;
    @Basic
    @Column(name = "egg_cycles", nullable = true)
    private Integer eggCycles;
    @Basic
    @Column(name = "against_normal", nullable = true, precision = 0)
    private Double againstNormal;
    @Basic
    @Column(name = "against_fire", nullable = true, precision = 0)
    private Double againstFire;
    @Basic
    @Column(name = "against_water", nullable = true, precision = 0)
    private Double againstWater;
    @Basic
    @Column(name = "against_electric", nullable = true, precision = 0)
    private Double againstElectric;
    @Basic
    @Column(name = "against_grass", nullable = true, precision = 0)
    private Double againstGrass;
    @Basic
    @Column(name = "against_ice", nullable = true, precision = 0)
    private Double againstIce;
    @Basic
    @Column(name = "against_fight", nullable = true, precision = 0)
    private Double againstFight;
    @Basic
    @Column(name = "against_poison", nullable = true, precision = 0)
    private Double againstPoison;
    @Basic
    @Column(name = "against_ground", nullable = true, precision = 0)
    private Double againstGround;
    @Basic
    @Column(name = "against_flying", nullable = true, precision = 0)
    private Double againstFlying;
    @Basic
    @Column(name = "against_psychic", nullable = true, precision = 0)
    private Double againstPsychic;
    @Basic
    @Column(name = "against_bug", nullable = true, precision = 0)
    private Double againstBug;
    @Basic
    @Column(name = "against_rock", nullable = true, precision = 0)
    private Double againstRock;
    @Basic
    @Column(name = "against_ghost", nullable = true, precision = 0)
    private Double againstGhost;
    @Basic
    @Column(name = "against_dragon", nullable = true, precision = 0)
    private Double againstDragon;
    @Basic
    @Column(name = "against_dark", nullable = true, precision = 0)
    private Double againstDark;
    @Basic
    @Column(name = "against_steel", nullable = true, precision = 0)
    private Double againstSteel;
    @Basic
    @Column(name = "against_fairy", nullable = true, precision = 0)
    private Double againstFairy;
}
