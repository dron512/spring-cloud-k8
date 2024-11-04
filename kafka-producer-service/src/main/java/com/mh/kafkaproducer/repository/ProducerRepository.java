package com.mh.kafkaproducer.repository;


import com.mh.kafkaproducer.entity.ProducerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProducerRepository extends JpaRepository<ProducerEntity,Integer> {

}
