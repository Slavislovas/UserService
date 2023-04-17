package com.fitnessapp.userservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.handler.FormError;
import com.fitnessapp.userservice.business.handler.FormErrorModel;
import com.fitnessapp.userservice.business.repository.UserRepository;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class UserIntegrationTest {

    @MockBean
    UserRepository userRepository;
    UserDto userDto;
    UserDto userDto2;
    UserCreationDto userCreationDto;
    UserEntity userEntity;
    UserEntity userEntity2;
    @Autowired
    MockMvc mockMvc;

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
                "TestEmail@gmail.com",
                "TestUsername");

        userDto2 = new UserDto("2L",
                "TestName2",
                "TestSurname2",
                LocalDate.now(),
                "TestEmail2@gmail.com",
                "TestUsername2");

        userCreationDto = new UserCreationDto("TestName",
                "TestSurname",
                LocalDate.now(),
                "TestEmail@gmail.com",
                "TestUsername",
                "Test@123");
    }

    @Test
    void findUserByIdSuccess() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(userDto);
        MvcResult mvcResult = mockMvc.perform(get("/user/findById/1L"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void findUserByIdNotFound() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/user/findById/1L"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findUserByUsernameSuccess() throws Exception {
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(userDto);
        MvcResult mvcResult = mockMvc.perform(get("/user/findByUsername/TestUsername"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void findUserByUsernameNotFound() throws Exception {
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/user/findByUsername/TestUsername"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findAllUsersSuccess() throws Exception {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userEntity, userEntity2));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(List.of(userDto, userDto2));
        MvcResult mvcResult = mockMvc.perform(get("/user/findAll"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void createUserSuccess() throws Exception {
        Mockito.when(userRepository.save(any())).thenReturn(userEntity);
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String requestBody = jsonMapper.writeValueAsString(userCreationDto);
        String expectedResult = jsonMapper.writeValueAsString(userDto);
        MvcResult mvcResult = mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void createUserAllDataInvalidException() throws Exception {
        userCreationDto.setName(null);
        userCreationDto.setName(null);
        userCreationDto.setEmail("TestEmailgmail.com");
        userCreationDto.setDateOfBirth(null);
        userCreationDto.setUsername(null);
        userCreationDto.setPassword("Test123");
        userCreationDto.setSurname(null);

        FormErrorModel formErrorModel = new FormErrorModel();
        formErrorModel.addFormError("username","must not be null");
        formErrorModel.addFormError("email","must be a well-formed email address");
        formErrorModel.addFormError("dateOfBirth","must not be null");
        formErrorModel.addFormError("name","must not be null");
        formErrorModel.addFormError("surname","must not be null");
        formErrorModel.addFormError("password","password must contain a minimum of 8 characters, have at last one uppercase letter,one lowercase letter, one number and one special character");
        formErrorModel.addFormError("username","must not be blank");
        formErrorModel.addFormError("surname","must not be blank");
        formErrorModel.addFormError("name","must not be blank");

        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String requestBody = jsonMapper.writeValueAsString(userCreationDto);
        MvcResult mvcResult = mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andReturn();
        List<FormError> expectedErrorList = formErrorModel.getErrors();
        List<FormError> resultErrorList = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), FormErrorModel.class).getErrors();
        assertTrue(expectedErrorList.containsAll(resultErrorList) && resultErrorList.containsAll(expectedErrorList));
    }

    @Test
    void createUserDuplicateUsernameException() throws Exception {
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String requestBody = jsonMapper.writeValueAsString(userCreationDto);

        FormErrorModel formErrorModel = new FormErrorModel();
        formErrorModel.addFormError("username", "username is taken");

        MvcResult mvcResult = mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andReturn();
        List<FormError> expectedErrorList = formErrorModel.getErrors();
        List<FormError> resultErrorList = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), FormErrorModel.class).getErrors();
        assertTrue( expectedErrorList.containsAll(resultErrorList) && resultErrorList.containsAll(expectedErrorList));
    }

    @Test
    void createUserDuplicateEmailException() throws Exception {
        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String requestBody = jsonMapper.writeValueAsString(userCreationDto);

        FormErrorModel formErrorModel = new FormErrorModel();
        formErrorModel.addFormError("email", "email is taken");

        MvcResult mvcResult = mockMvc.perform(post("/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andReturn();
        List<FormError> expectedErrorList = formErrorModel.getErrors();
        List<FormError> resultErrorList = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), FormErrorModel.class).getErrors();
        assertTrue( expectedErrorList.containsAll(resultErrorList) && resultErrorList.containsAll(expectedErrorList));
    }
}
