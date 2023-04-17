package com.fitnessapp.userservice.business.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormError {
    private String field;
    private String errorMessage;
}
