package com.sai.BGClear.controller;

import com.sai.BGClear.dto.UserDTO;
import com.sai.BGClear.response.ClearBgResponse;
import com.sai.BGClear.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ClearBgResponse createOrUpdateUser(@RequestBody UserDTO userDTO) {
        try{
            UserDTO user = userService.saveUser(userDTO);
            return ClearBgResponse.builder()
                    .success(true)
                    .data(user)
                    .statusCode(HttpStatus.CREATED)
                    .build();
        }catch (Exception exception){
            return ClearBgResponse.builder()
                    .success(false)
                    .data(exception.getMessage())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
