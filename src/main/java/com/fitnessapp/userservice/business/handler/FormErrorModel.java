package com.fitnessapp.userservice.business.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormErrorModel {
    ArrayList<FormError> errors;

    public FormErrorModel(){
        errors = new ArrayList<>();
    }

    public void addFormError(String field, String message){
        errors.add(new FormError(field, message));
    }
}
