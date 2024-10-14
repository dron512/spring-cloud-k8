package org.mh.client.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/first/test")
    public String test(){
        return "first Test";
    }

    @GetMapping("/first/message")
    public String message(@RequestHeader("first-request") String header){
        System.out.println(header);
        return "first Message";
    }
}
