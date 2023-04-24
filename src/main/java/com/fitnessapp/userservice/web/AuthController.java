package com.fitnessapp.userservice.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null){
            return new ResponseEntity<>("Login failed, no Basic Auth header found", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("You have logged in successfully");
    }
}
