package org.mh.client.test;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final Environment environment;

    @GetMapping("/first/test")
    @Timed(value = "first-test", description = "Time taken to process first test")
    public String test(){
        return """
                first Test environment.getProperty("mh.value") = %s
               """.formatted(environment.getProperty("mh.value"));
    }

    @GetMapping("/first/message")
    @Timed(value = "first-message", description = "Time taken to process first message")
    public String message(@RequestHeader("first-request") String header){
        System.out.println(header);
        return "first Message";
    }
}
