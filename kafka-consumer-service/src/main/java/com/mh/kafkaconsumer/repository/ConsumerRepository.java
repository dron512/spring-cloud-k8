package com.mh.kafkaconsumer.repository;


import com.mh.kafkaconsumer.entity.ConsumerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<ConsumerEntity,Long> {
}
