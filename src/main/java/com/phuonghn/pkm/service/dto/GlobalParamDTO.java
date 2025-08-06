package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalParamDTO {

    private Long id;
    private String code;
    private String value;
    private String type;
    private String description;
}
