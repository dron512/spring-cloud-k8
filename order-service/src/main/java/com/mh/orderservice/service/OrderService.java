package com.mh.orderservice.service;

import com.mh.orderservice.dto.OrderReqDto;
import com.mh.orderservice.dto.OrderResDto;
import com.mh.orderservice.entity.OrderEntity;
import com.mh.orderservice.messagequeue.OrderProducer;
import com.mh.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService  {
    private final OrderRepository orderRepository;

    private final Environment env;
    private final ModelMapper modelMapper;

    private final OrderProducer orderProducer;

    public OrderResDto createOrder(OrderReqDto orderReqDto) {
        orderReqDto.setOrderId(UUID.randomUUID().toString());
        orderReqDto.setTotalPrice(orderReqDto.getQty() * orderReqDto.getUnitPrice());

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity orderEntity = modelMapper.map(orderReqDto, OrderEntity.class);

        orderEntity = orderRepository.save(orderEntity);

        //kafka Messsage cend
       orderProducer.sendMessage(orderEntity);

        OrderResDto orderResDto = modelMapper.map(orderEntity, OrderResDto.class);

        return orderResDto;
    }

    public OrderResDto getOrderByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
        OrderResDto orderDto = new ModelMapper().map(orderEntity, OrderResDto.class);

        return orderDto;
    }

    public List<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }



}