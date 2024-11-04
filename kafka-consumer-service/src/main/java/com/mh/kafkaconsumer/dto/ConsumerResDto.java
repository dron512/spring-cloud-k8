package com.mh.kafkaconsumer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsumerResDto {
    @Schema(defaultValue = "producer-001")
    private String name;
    @Schema(defaultValue = "100")
    private int quantity;

    @Schema(defaultValue = "1")
    private Long id;
}
