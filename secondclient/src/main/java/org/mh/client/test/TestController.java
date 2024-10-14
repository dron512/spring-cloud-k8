package org.mh.client.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/second/test")
    public String test(){
        return "second Test";
    }

    @GetMapping("/second/message")
    public String message(@RequestHeader("second-request") String header){
        System.out.println(header);
        return "second Message";
    }
}
