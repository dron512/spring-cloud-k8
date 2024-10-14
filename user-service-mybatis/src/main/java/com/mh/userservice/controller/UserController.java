package com.mh.userservice.controller;

import com.mh.userservice.config.AuthenticationFilter;
import com.mh.userservice.dto.UserDto;
import com.mh.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Environment environment;

    @GetMapping("/env")
    public String env(){
        return String.format("local.server.port =  %s",environment.getProperty("local.server.port"));
    }

    @GetMapping("/add-user")
    public String user(){
        userService.createUser(UserDto.builder().user_id("test").user_email("test").user_password("test").user_name("test").build());
        return "User";
    }

    @GetMapping("/login")
    public void login(String user_email, String user_password, HttpServletResponse res) throws IOException {
        res.sendRedirect("/users/login?user_email="+user_email+"&user_password="+user_password);
    }


}
