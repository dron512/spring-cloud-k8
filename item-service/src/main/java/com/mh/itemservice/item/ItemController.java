package com.mh.itemservice.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-service")
public class ItemController {

    private final Environment environment;

    @Value("${mh.value}")
    private String mhValue;

    @GetMapping("/item")
    public String item(){
        System.out.println(environment.getProperty("local.server.port"));
        return "Item"+environment.getProperty("local.server.port");
    }

    @GetMapping("/mhvalue")
    public String mhvalue(){
        return "Item "+environment.getProperty("mh.value")+" "+mhValue;
    }
}
