package com.mh.kafkaproducer.messagequeue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.kafkaproducer.dto.ProducerReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, ProducerReqDto message) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try
        {
            json = mapper.writeValueAsString(message);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        kafkaTemplate.send(topic, json);
        log.info("Message sent to topic: " + topic + " with value: " + json);

    }
}
