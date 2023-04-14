package com.fitnessapp.userservice.web;

import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.model.UserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                                                    @PathVariable("id") Long userId){
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
}
