package com.fitnessapp.userservice.business.handler;

import javax.servlet.http.HttpServletRequest;

import com.fitnessapp.userservice.business.handler.exception.DuplicateDataException;
import com.fitnessapp.userservice.business.handler.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorModel> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request){
        ErrorModel errorModel = new ErrorModel(HttpStatus.NOT_FOUND.value(),
                                               ex.getMessage(),
                                               LocalDate.now(),
                                               request.getRequestURI());
        return new ResponseEntity<ErrorModel>(errorModel, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<FormErrorModel> handleDuplicateDataException(DuplicateDataException ex){
        return new ResponseEntity<FormErrorModel>(ex.getErrorModel(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<FormErrorModel> handleInvalidDataException(InvalidDataException ex){
        return new ResponseEntity<FormErrorModel>(ex.getErrorModel(), HttpStatus.BAD_REQUEST);
    }
}
