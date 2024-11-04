package com.mh.userservice.feignclient;

import com.mh.userservice.error.OrderFeignErrorDecoder;
import com.mh.userservice.vo.ResponseOrder;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="ORDER-SERVICE", configuration = OrderFeignErrorDecoder.class)
//@LoadBalancerClient(name = "127.0.0.1")
public interface OrderServiceClient {

    @GetMapping("/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable(value = "userId") String userId);

}
