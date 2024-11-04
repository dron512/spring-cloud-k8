package com.mh.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.orderservice.dto.OrderReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProdcer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderReqDto sendMessage(String topic, OrderReqDto orderReqDto) {
        ObjectMapper mapper = new ObjectMapper();
        String message = null;
        try {
            message = mapper.writeValueAsString(orderReqDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(topic, message);
        log.info("Sending message '{}' to topic '{}'", message, topic);
        return orderReqDto;
    }

}
