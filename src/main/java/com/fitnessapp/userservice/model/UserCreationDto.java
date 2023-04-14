package com.fitnessapp.userservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fitnessapp.userservice.business.util.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.time.LocalDate;

@ApiModel(description = "User dto used for creating new users")
public class UserCreationDto {
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

    @NotNull
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(name = "password",
            notes = "password of the user, minimum eight characters," +
                    " at least one uppercase letter, one lowercase letter," +
                    " one number and one special character",
            example = "Test@123")
    private String password;
}
