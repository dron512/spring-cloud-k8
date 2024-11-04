package com.mh.userservice.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class CatalogFeignErrorDecoder implements ErrorDecoder {

    private final Environment environment;

    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404:
                if (s.contains("getCatalogs")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            "Not catalogs");
                }
                break;
            default:
                return new Exception(response.reason());
        }
        return null;
    }
}
