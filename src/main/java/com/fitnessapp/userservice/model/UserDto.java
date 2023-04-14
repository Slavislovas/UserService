package com.fitnessapp.userservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fitnessapp.userservice.business.util.Constants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "user data transfer object")
public class UserDto {
    @NotNull
    @ApiModelProperty(name = "id", notes = "id of the user", example = "123")
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 25)
    @ApiModelProperty(name = "name", notes = "name of the user, maximum characters: 25", example = "John")
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 25)
    @ApiModelProperty(name = "surname", notes = "surname of the user, maximum characters: 25", example = "Doe")
    private String surname;

    @NotNull
    @JsonFormat(pattern = Constants.dateFormat)
    @ApiModelProperty(name = "dateOfBirth", notes = "date of birth of the user", example = "2001-11-16")
    private LocalDate dateOfBirth;

    @NotNull
    @Email
    @ApiModelProperty(name = "email", notes = "email of the user", example = "JohnDoe@gmail.com")
    private String email;

    @NotNull
    @NotBlank
    @Size(max = 25)
    @ApiModelProperty(name = "username", notes = "username of the user, maximum characters: 25", example = "JohnDoe")
    private String username;
}
