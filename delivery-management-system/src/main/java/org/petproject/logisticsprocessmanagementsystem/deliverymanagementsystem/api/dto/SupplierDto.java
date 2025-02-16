package org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SupplierDto {

    Long id;

    String status;

    Long orderId;
}
