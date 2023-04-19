package com.fitnessapp.userservice.controller;

import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.handler.exception.DuplicateDataException;
import com.fitnessapp.userservice.business.handler.exception.InvalidDataException;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import com.fitnessapp.userservice.model.UserEditDto;
import com.fitnessapp.userservice.web.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    UserEntity userEntity2;
    UserDto userDto;
    UserDto userDto2;
    UserCreationDto userCreationDto;
    UserEditDto userEditDto;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity("1L",
                "TestName",
                "TestSurname",
                LocalDate.now(),
                "TestEmail@gmail.com",
                "TestUsername",
                "$2a$12$MoUsV1PVKz47hfzEiBF7Mef5f8AScPFGI/G4vjC1VvE60Md5yX7K.", // password: Test@123
                Role.ROLE_USER);

        userEntity2 = new UserEntity("2L",
                "TestName2",
                "TestSurname2",
                LocalDate.now(),
                "TestEmail2@gmail.com",
                "TestUsername2",
                "$2a$12$MoUsV1PVKz47hfzEiBF7Mef5f8AScPFGI/G4vjC1VvE60Md5yX7K.", // password: Test@123
                Role.ROLE_USER);

        userDto = new UserDto("1L",
                "TestName",
                "TestSurname",
                LocalDate.now(),
                "TestEmail@Gmail.com",
                "TestUsername");

        userDto2 = new UserDto("2L",
                "TestName2",
                "TestSurname2",
                LocalDate.now(),
                "TestEmail2@Gmail.com",
                "TestUsername2");

        userCreationDto = new UserCreationDto("TestName",
                "TestSurname",
                LocalDate.now(),
                "TestEmail@gmail.com",
                "TestUsername",
                "$2a$12$MoUsV1PVKz47hfzEiBF7Mef5f8AScPFGI/G4vjC1VvE60Md5yX7K."); // password: Test@123

        userEditDto = new UserEditDto("EditTestName",
                "EditTestSurname",
                LocalDate.now(),
                "EditTestEmail@gmail.com");
    }

    @Test
    void findUserByIdSuccess() {
        Mockito.when(userService.findUserById(any())).thenReturn(userDto);
        ResponseEntity<UserDto> result = userController.findUserById("1L");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @Test
    void findUserByIdNotFound(){
        Mockito.when(userService.findUserById(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> userController.findUserById("1L"));
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

    @Test
    void findAllUsersSuccess(){
        List<UserDto> userDtoList = List.of(userDto, userDto2);
        Mockito.when(userService.findAllUsers()).thenReturn(userDtoList);
        ResponseEntity<List<UserDto>> result = userController.findAllUsers();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDtoList, result.getBody());
    }

    @Test
    void createUserSuccess(){
        Mockito.when(userService.createUser(userCreationDto)).thenReturn(userDto);
        ResponseEntity<UserDto> result = userController.createUser(userCreationDto, new MapBindingResult(Collections.emptyMap(), "errors"));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @Test
    void createUserDuplicateDataException(){
        Mockito.when(userService.createUser(any())).thenThrow(DuplicateDataException.class);
        assertThrows(DuplicateDataException.class, () -> userController.createUser(userCreationDto, new MapBindingResult(Collections.emptyMap(), "errors")));
    }

    @Test
    void createUserInvalidDataException(){
        MapBindingResult mapBindingResult = new MapBindingResult(Collections.emptyMap(), "errors");
        mapBindingResult.addError(new ObjectError("email", "must be a well-formed email"));
        assertThrows(InvalidDataException.class, () -> userController.createUser(userCreationDto, mapBindingResult));
    }

    @Test
    void editUserSuccess(){
        userDto.setName(userEditDto.getName());
        userDto.setSurname(userEditDto.getSurname());
        userDto.setEmail(userEditDto.getEmail());
        userDto.setDateOfBirth(userEditDto.getDateOfBirth());
        Mockito.when(userService.editUser(any(), any())).thenReturn(userDto);
        ResponseEntity<UserDto> result = userController.editUser("1L", userEditDto, new MapBindingResult(Collections.emptyMap(), "errors"));
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userDto, result.getBody());
    }

    @Test
    void editUserDuplicateEmailException(){
        userDto.setName(userEditDto.getName());
        userDto.setSurname(userEditDto.getSurname());
        userDto.setEmail(userEditDto.getEmail());
        userDto.setDateOfBirth(userEditDto.getDateOfBirth());
        Mockito.when(userService.editUser(any(), any())).thenThrow(DuplicateDataException.class);
        assertThrows(DuplicateDataException.class, () -> userController.editUser("1L", userEditDto, new MapBindingResult(Collections.emptyMap(), "errors")));
    }

    @Test
    void editUserInvalidDataException(){
        MapBindingResult mapBindingResult = new MapBindingResult(Collections.emptyMap(), "errors");
        mapBindingResult.addError(new ObjectError("email", "must be a well-formed email"));
        assertThrows(InvalidDataException.class, () -> userController.editUser("1L", userEditDto, mapBindingResult));
    }

    @Test
    void deleteUserSuccess(){
        Mockito.when(userService.deleteUserById(any())).thenReturn("User with id: 1L has been deleted successfully");
        ResponseEntity<String> result = userController.deleteUserById("1L");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User with id: 1L has been deleted successfully", result.getBody());
    }

    @Test
    void deleteUserNotFoundException(){
        Mockito.when(userService.deleteUserById(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> userController.deleteUserById("1L"));
    }
}