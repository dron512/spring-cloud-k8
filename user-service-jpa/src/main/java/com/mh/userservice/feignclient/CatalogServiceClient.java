package com.mh.userservice.feignclient;

import com.mh.userservice.error.CatalogFeignErrorDecoder;
import com.mh.userservice.vo.ResponseCatalog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="CATALOG-SERVICE",configuration = CatalogFeignErrorDecoder.class)
public interface CatalogServiceClient {

    @GetMapping("/catalogs")
    List<ResponseCatalog> getCatalogs();

    @GetMapping("/")
    String test();
}