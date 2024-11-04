package com.mh.kafkaproducer.service;

import com.mh.kafkaproducer.dto.ProducerReqDto;
import com.mh.kafkaproducer.dto.ProducerResDto;
import com.mh.kafkaproducer.entity.ProducerEntity;
import com.mh.kafkaproducer.messagequeue.KafkaProducer;
import com.mh.kafkaproducer.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService  {
    private final ProducerRepository producerRepository;

    private final Environment env;
    private final ModelMapper modelMapper;

    private final KafkaProducer kafkaProducer;

    public ProducerResDto createProducer(ProducerReqDto producerReqDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProducerEntity producerEntity = modelMapper.map(producerReqDto, ProducerEntity.class);

        producerRepository.save(producerEntity);

        ProducerResDto orderResDto = modelMapper.map(producerEntity, ProducerResDto.class);

        // kafka send
        kafkaProducer.send("topic1", producerReqDto);

        return orderResDto;
    }

    public List<ProducerResDto> getAll() {
        List<ProducerEntity> producerEntity = producerRepository.findAll();

        List<ProducerResDto> producerResDtos =
        producerEntity.stream().map(producer -> modelMapper.map(producer, ProducerResDto.class))
                .toList();

        return producerResDtos;
    }


}