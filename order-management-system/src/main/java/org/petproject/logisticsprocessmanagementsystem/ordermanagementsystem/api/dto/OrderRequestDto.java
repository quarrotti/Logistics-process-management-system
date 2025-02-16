package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

    String description;

    ProductDetailsDto productDetails;

}
