package com.mh.catalogservice.controller;

import com.mh.catalogservice.dto.CatalogResDto;
import com.mh.catalogservice.entity.CatalogEntity;
import com.mh.catalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CatalogController {

    private final CatalogService catalogService;
    private final Environment environment;
    private final ModelMapper modelMapper;
    private final DiscoveryClient discoveryClient;

    @GetMapping("/")
    public ResponseEntity<String> getCatalog() {
        return ResponseEntity.ok("루트페이지");
    }

    @GetMapping("/health-check")
    public String status() {
        List<ServiceInstance> serviceList = getApplications();
        for (ServiceInstance instance : serviceList) {
            System.out.println(String.format("instanceId:%s, serviceId:%s, host:%s, scheme:%s, uri:%s",
                    instance.getInstanceId(), instance.getServiceId(), instance.getHost(), instance.getScheme(), instance.getUri()));
        }

        return String.format("It's Working in Catalog Service on LOCAL PORT %s (SERVER PORT %s)",
                environment.getProperty("local.server.port"),
                environment.getProperty("server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<CatalogResDto>> getCatalogs() {
        Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();

        List<CatalogResDto> result = new ArrayList<>();
        catalogList.forEach(v -> {
            result.add(new ModelMapper().map(v, CatalogResDto.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    private List<ServiceInstance> getApplications() {
        List<String> services = this.discoveryClient.getServices();
        List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
        services.forEach(serviceName -> {
            this.discoveryClient.getInstances(serviceName).forEach(instance ->{
                instances.add(instance);
            });
        });
        return instances;
    }
}
