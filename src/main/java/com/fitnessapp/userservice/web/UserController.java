package com.fitnessapp.userservice.web;

import com.fitnessapp.userservice.business.handler.FormErrorModel;
import com.fitnessapp.userservice.business.handler.exception.InvalidDataException;
import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import com.fitnessapp.userservice.model.UserEditDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "Finds user data by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 404, message = "user does not exist")
    })
    @GetMapping("/findById/{id}")
    public ResponseEntity<UserDto> findUserById(@ApiParam(value = "Id of the user")
                                                    @PathVariable("id") String userId){
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @ApiOperation(value = "Finds user data by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 404, message = "user does not exist")
    })
    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<UserDto> findUserByUsername(@ApiParam(value = "username of the user")
                                                          @PathVariable("username") String username){
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @ApiOperation(value = "Finds all user data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
    })
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @ApiOperation(value = "creates a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 409, message = "Username and/or email is taken"),
    })
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreationDto userCreationDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            buildFormErrorModelAndThrowException(bindingResult);
        }
        return ResponseEntity.ok(userService.createUser(userCreationDto));
    }

    @ApiOperation(value = "edits user data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "Email is taken"),
    })
    @PutMapping("/edit/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable("id") String id,
                                            @Valid @RequestBody UserEditDto userCreationDto,
                                            BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            buildFormErrorModelAndThrowException(bindingResult);
        }
        return ResponseEntity.ok(userService.editUser(id, userCreationDto));
    }

    private static void buildFormErrorModelAndThrowException(BindingResult bindingResult) {
        FormErrorModel formErrorModel = new FormErrorModel();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            formErrorModel.addFormError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        throw new InvalidDataException(formErrorModel);
    }

    @ApiOperation(value = "deletes user data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successful"),
            @ApiResponse(code = 404, message = "User not found"),
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") String id){
        return ResponseEntity.ok(userService.deleteUserById(id));
    }
}
