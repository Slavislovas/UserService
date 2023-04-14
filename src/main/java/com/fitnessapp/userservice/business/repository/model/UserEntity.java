package com.fitnessapp.userservice.business.repository.model;

import com.fitnessapp.userservice.business.enumerator.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class UserEntity {

    @Id
    @Field("id")
    private Long id;

    @Field("name")
    private String name;

    @Field("surname")
    private String surname;

    @Field("dateOfBirth")
    private LocalDate dateOfBirth;

    @Field("email")
    private String email;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("Role")
    Role role;
}
