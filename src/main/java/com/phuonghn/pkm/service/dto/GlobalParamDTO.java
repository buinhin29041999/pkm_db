package com.phuonghn.pkm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalParamDTO {

    private Long id;
    private String code;
    private String value;
    private String type;
    private List<String> types;
    private String description;
    private Long position;
    private String icon;
    public GlobalParamDTO(Long id, String code, String value, String type, String description, String icon) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.type = type;
        this.description = description;
        this.icon = icon;
    }
}
