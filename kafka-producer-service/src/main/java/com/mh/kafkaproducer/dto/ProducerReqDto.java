package com.mh.kafkaproducer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerReqDto {
    @Schema(defaultValue = "producer-001")
    private String name;
    @Schema(defaultValue = "100")
    private int quantity;

    @Schema(defaultValue = "1")
    private Long id;
}
