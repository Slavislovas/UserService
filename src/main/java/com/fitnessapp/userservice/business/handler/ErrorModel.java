package com.fitnessapp.userservice.business.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private int statusCode;
    private String message;
    private LocalDate timeStamp;
    private String path;
}
