package com.mh.userservice.controller;

import com.mh.userservice.dto.UserReqDto;
import com.mh.userservice.dto.UserResDto;
import com.mh.userservice.entity.UserEntity;
import com.mh.userservice.feignclient.CatalogServiceClient;
import com.mh.userservice.service.UserService;
import com.mh.userservice.vo.ResponseUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final Environment environment;

    private final CatalogServiceClient catalogServiceClient;

    @GetMapping("/cata")
    public String cata(){
        String test = catalogServiceClient.test();
        log.info(test);
        return test;
    }

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

    @GetMapping("/users")
    public List<UserResDto> getUsers(){
        List<UserEntity> list = userService.getUserByAll();
        return list.stream().map(userEntity -> new ModelMapper().map(userEntity,UserResDto.class)).toList();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") String userId) {
        UserResDto userResDto = userService.getUserByUserId(userId);

        if (userResDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ResponseUser returnValue = new ModelMapper().map(userResDto, ResponseUser.class);

        System.out.println(returnValue);

        EntityModel entityModel = EntityModel.of(returnValue);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getUsers());
        entityModel.add(linkTo.withRel("all-users"));

        try {
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }



}
