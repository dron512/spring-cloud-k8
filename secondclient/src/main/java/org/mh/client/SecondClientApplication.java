package org.mh.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
public class SecondClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondClientApplication.class, args);
    }

}
