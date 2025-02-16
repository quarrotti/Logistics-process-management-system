package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {

    Long id;

    LocalDateTime created_at;

    String description;

    String executionDetails;

    String status;

}
