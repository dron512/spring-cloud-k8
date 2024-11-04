package com.mh.kafkaconsumer.controller;

import com.mh.kafkaconsumer.dto.ConsumerReqDto;
import com.mh.kafkaconsumer.dto.ConsumerResDto;
import com.mh.kafkaconsumer.entity.ConsumerEntity;
import com.mh.kafkaconsumer.service.ConsumerService;
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
public class KafkaConsumerController {

    private final ConsumerService consumerService;
    private final Environment environment;
    private final ModelMapper modelMapper;
    private final DiscoveryClient discoveryClient;

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

    @GetMapping("/consumers")
    public ResponseEntity<List<ConsumerResDto>> getConsumers() {
        Iterable<ConsumerEntity> consumerEntities = consumerService.getAllConsumers();

        List<ConsumerResDto> result = new ArrayList<>();
        consumerEntities.forEach(v -> {
            result.add(new ModelMapper().map(v, ConsumerResDto.class));
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
