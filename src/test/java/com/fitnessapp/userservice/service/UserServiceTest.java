package com.fitnessapp.userservice.service;

import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.handler.exception.DuplicateDataException;
import com.fitnessapp.userservice.business.mapper.UserMapStruct;
import com.fitnessapp.userservice.business.repository.UserRepository;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.business.service.impl.UserServiceImpl;
import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import com.fitnessapp.userservice.model.UserEditDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapStruct userMapStruct;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    UserEntity userEntity;
    UserEntity editedUserEntity;
    UserEntity userEntity2;
    UserDto userDto;
    UserDto editedUserDto;
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

        editedUserEntity = new UserEntity("1L",
                "EditTestName",
                "EditTestSurname",
                LocalDate.now(),
                "EditTestEmail@gmail.com",
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

        editedUserDto = new UserDto("1L",
                "EditTestName",
                "EditTestSurname",
                LocalDate.now(),
                "EditTestEmail@gmail.com",
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
                "Test@123");

        userEditDto = new UserEditDto("EditTestName",
                "EditTestSurname",
                LocalDate.now(),
                "EditTestEmail@gmail.com");
    }

    @Test
    void findUserByIdSuccess(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapStruct.entityToDto(any())).thenReturn(userDto);

        UserDto result = userService.findUserById("1L");
        assertEquals(userDto, result);
    }

    @Test
    void findUserByIdNotFound(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.findUserById("1L"));
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

    @Test
    void findAllUsersSuccess(){
        List<UserDto> userDtoList = List.of(userDto, userDto2);
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userEntity, userEntity2));
        Mockito.when(userMapStruct.entityToDto(userEntity)).thenReturn(userDto);
        Mockito.when(userMapStruct.entityToDto(userEntity2)).thenReturn(userDto2);

        List<UserDto> results = userService.findAllUsers();
        assertEquals(userDtoList, results);
    }

    @Test
    void createUserSuccess(){
        Mockito.when(userMapStruct.creationDtoToEntity(any())).thenReturn(userEntity);
        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
        Mockito.when(userMapStruct.entityToDto(any())).thenReturn(userDto);
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(any())).thenReturn("$2a$12$MoUsV1PVKz47hfzEiBF7Mef5f8AScPFGI/G4vjC1VvE60Md5yX7K.");

        UserDto result = userService.createUser(userCreationDto);

        assertEquals(userDto, result);
    }

    @Test
    void createUserDuplicateUsernameException(){
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(DuplicateDataException.class, () -> userService.createUser(userCreationDto));
    }

    @Test
    void createUserDuplicateEmailException(){
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        assertThrows(DuplicateDataException.class, () -> userService.createUser(userCreationDto));
    }

    @Test
    void editUserSuccess(){
        Mockito.when(userRepository.save(any())).thenReturn(editedUserEntity);
        Mockito.when(userMapStruct.entityToDto(any())).thenReturn(editedUserDto);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userEntity));
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        UserDto result = userService.editUser("1L", userEditDto);
        assertEquals(editedUserDto, result);
    }

    @Test
    void editUserNotFoundException(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.editUser("1L", userEditDto));
    }

    @Test
    void editUserDuplicateEmailException(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity2));
        assertThrows(DuplicateDataException.class, () -> userService.editUser("1L", userEditDto));
    }

    @Test
    void deleteUserSuccessful(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(any());
        String result = userService.deleteUserById("1L");
        assertEquals("User with id: 1L has been deleted successfully", result);
    }

    @Test
    void deleteUserNotFoundException(){
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());
        doNothing().when(userRepository).deleteById(any());
        assertThrows(NoSuchElementException.class, () -> userService.deleteUserById("1L"));
    }
}
