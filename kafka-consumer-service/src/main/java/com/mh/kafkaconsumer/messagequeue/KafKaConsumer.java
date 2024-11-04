package com.mh.kafkaconsumer.messagequeue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mh.kafkaconsumer.entity.ConsumerEntity;
import com.mh.kafkaconsumer.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafKaConsumer {

    private final ConsumerRepository consumerRepository;

    @KafkaListener(topics = "topic1")
    public void updateQuantity(String kafkaMessage){
        log.info("kafka Message ->"+kafkaMessage);

        ObjectMapper mapper = new ObjectMapper();
        ConsumerEntity consumerEntity = null;
        try{
            consumerEntity = mapper.readValue(kafkaMessage, ConsumerEntity.class);
            System.out.println(consumerEntity);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(consumerEntity.getId());
        long id = consumerEntity.getId()==null? 0: consumerEntity.getId();

        Optional<ConsumerEntity> optionalConsumerEntity = consumerRepository.findById(id);
        if(optionalConsumerEntity.isPresent()){
            ConsumerEntity consumerDBEntity = optionalConsumerEntity.get();
            consumerDBEntity.setQuantity(consumerEntity.getQuantity() + consumerEntity.getQuantity());
            consumerRepository.save(consumerDBEntity);
        }else
        {
            consumerRepository.save(consumerEntity);
        }
        log.info("kafka Message completed->"+kafkaMessage);
    }


}
