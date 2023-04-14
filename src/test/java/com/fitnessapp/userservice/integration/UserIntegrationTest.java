package com.fitnessapp.userservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.repository.UserRepository;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.model.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

@AutoConfigureMockMvc
@SpringBootTest
public class UserIntegrationTest {

    @MockBean
    UserRepository userRepository;
    UserDto userDto;
    UserEntity userEntity;
    @Autowired
    MockMvc mockMvc;

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
                "TestEmail@gmail.com",
                "TestUsername");
    }

    @Test
    void findUserByIdSuccess() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        JsonMapper jsonMapper = new JsonMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        String expectedResult = jsonMapper.writeValueAsString(userDto);
        MvcResult mvcResult = mockMvc.perform(get("/user/findById/1"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResult, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void findUserByIdNotFound() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(get("/user/findById/1"))
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
}
