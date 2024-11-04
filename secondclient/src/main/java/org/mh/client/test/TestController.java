package org.mh.client.test;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final Environment environment;

    @GetMapping("/second/test")
    public String test(HttpServletRequest request){
        String apiGatewayString = request.getHeader("Req-first-Header");
        System.out.println("apiGatewayString:"+apiGatewayString);
        return """
                second Test environment.getProperty("mh.value") = %s
               """.formatted(environment.getProperty("mh.value"));
    }

    @GetMapping("/second/message")
    public String message(@RequestHeader("second-request") String header){
        System.out.println(header);
        return "second Message";
    }
}
