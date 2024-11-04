package com.mh.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.orderservice.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(OrderEntity.class.getDeclaredFields());

    public void sendMessage(OrderEntity orderEntity) {
        String topic = "topic";

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(orderEntity);
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString.toString());
        log.info("Order message sent to Kafka: {}", jsonInString.toString());

//       StringBuilder message = new StringBuilder();
//       for (Field field : fields) {
//           field.setAccessible(true);
//           try {
//               message.append(field.getName()).append(": ").append(field.get(orderEntity)).append(", ");
//           } catch (IllegalAccessException e) {
//               log.error("Error accessing field: {}", e.getMessage());
//           }
//       }
//
//       kafkaTemplate.send(topic, message.toString());
//       log.info("Order message sent to Kafka: {}", message.toString());
    }
}
