package com.mh.userservice.feignclient;

import com.mh.userservice.error.CatalogFeignErrorDecoder;
import com.mh.userservice.vo.ResponseCatalog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="http://localhost:10002",configuration = CatalogFeignErrorDecoder.class)
public interface CatalogServiceClient {

    @GetMapping("/catalogs")
    List<ResponseCatalog> getCatalogs();

    @GetMapping("/")
    String test();
}