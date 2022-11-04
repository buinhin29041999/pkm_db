package com.phuonghn.pkm.service.dto;

import lombok.Data;

@Data
public class TypeDetailDTO {
    private Long id;
    private Long typeAId;
    private Long typeBId;
    private Double aAttackB;
    private Double bAttackA;

}
