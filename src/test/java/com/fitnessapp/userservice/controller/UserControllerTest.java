package com.fitnessapp.userservice.controller;

import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.model.UserDto;
import com.fitnessapp.userservice.web.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    UserEntity userEntity;
    UserDto userDto;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity(1L,
                "TestName",
                "TestSurname",
                LocalDate.now(),
                "TestEmail@gmail.com",
                "TestUsername",
                "$2a$12$MoUsV1PVKz47hfzEiBF7Mef5f8AScPFGI/G4vjC1VvE60Md5yX7K.", // password: Test@123
                Role.ROLE_USER);

        userDto = new UserDto(1L,
                "TestName",
                "TestSurname",
                LocalDate.now(),
                "TestEmail@Gmail.com",
                "TestUsername");
    }

    @Test
    void findUserByIdSuccess() {
        Mockito.when(userService.findUserById(any())).thenReturn(userDto);
        ResponseEntity<UserDto> result = userController.findUserById(1L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @Test
    void findUserByIdNotFound(){
        Mockito.when(userService.findUserById(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> userController.findUserById(1L));
    }

    @Test
    void findUserByUsernameSuccess() {
        Mockito.when(userService.findByUsername(any())).thenReturn(userDto);
        ResponseEntity<UserDto> result = userController.findUserByUsername("TestUsername");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @Test
    void findUserByUsernameNotFound(){
        Mockito.when(userService.findByUsername(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> userController.findUserByUsername("TestUsername"));
    }
}