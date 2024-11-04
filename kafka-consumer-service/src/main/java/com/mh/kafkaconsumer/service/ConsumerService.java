package com.mh.kafkaconsumer.service;

import com.mh.kafkaconsumer.entity.ConsumerEntity;
import com.mh.kafkaconsumer.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerService {
    private final ConsumerRepository consumerRepository;

    public Iterable<ConsumerEntity> getAllConsumers() {
        return consumerRepository.findAll();
    }

}