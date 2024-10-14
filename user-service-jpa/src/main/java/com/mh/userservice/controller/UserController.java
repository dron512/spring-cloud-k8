package com.mh.userservice.controller;

import com.mh.userservice.dto.UserReqDto;
import com.mh.userservice.dto.UserResDto;
import com.mh.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final Environment environment;

    @GetMapping("/env")
    public String env(){
        log.info("mh.value = {}",environment.getProperty("mh.value"));
        log.info("spring.datasource.password = {}",environment.getProperty("spring.datasource.password"));

        return String.format("local.server.port =  %s\n " +
                "mh.value =  %s",
                environment.getProperty("local.server.port")
                ,environment.getProperty("mh.value"));
    }

    @PostMapping("/add-user")
    public ResponseEntity<UserResDto> user(@Valid @RequestBody UserReqDto userReqDto){
        UserResDto userResDto = userService.createUser(userReqDto);
        return ResponseEntity.ok(userResDto);
    }

    @GetMapping("/login")
    public void login(String email, String password, HttpServletResponse res) throws IOException {
        res.sendRedirect("/users/login?email="+email+"&password="+password);
    }


}
