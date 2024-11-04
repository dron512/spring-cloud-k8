package com.mh.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderFeignErrorDecoder implements ErrorDecoder {

    private final Environment environment;

    @Override
    public Exception decode(String s, Response response) {
        log.error("Order service error: {}", s);
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if(s.contains("getOrders"))
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "User order is empty");
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
