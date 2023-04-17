package com.fitnessapp.userservice.business.handler.exception;

import com.fitnessapp.userservice.business.handler.FormErrorModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvalidDataException extends RuntimeException{
    private FormErrorModel errorModel;
}
