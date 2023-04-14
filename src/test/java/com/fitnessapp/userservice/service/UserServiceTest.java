package com.fitnessapp.userservice.service;

import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.mapper.UserMapStruct;
import com.fitnessapp.userservice.business.repository.UserRepository;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.business.service.impl.UserServiceImpl;
import com.fitnessapp.userservice.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapStruct userMapStruct;

    @Spy
    @InjectMocks
    UserServiceImpl userService;

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
    void findUserByIdSuccess(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapStruct.entityToDto(any())).thenReturn(userDto);

        UserDto result = userService.findUserById(1L);
        assertEquals(userDto, result);
    }

    @Test
    void findUserByIdNotFound(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findUserById(1L));
    }

    @Test
    void findUserByUsernameSuccess(){
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapStruct.entityToDto(any())).thenReturn(userDto);

        UserDto result = userService.findByUsername("TestUsername");
        assertEquals(userDto, result);
    }

    @Test
    void findUserByUsernameNotFound(){
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findByUsername("TestUsername"));
    }
}
