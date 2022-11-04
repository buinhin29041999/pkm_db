package com.phuonghn.pkm.common.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private final Date timestamp = new Date();
    private String message;
    private String description;
}
