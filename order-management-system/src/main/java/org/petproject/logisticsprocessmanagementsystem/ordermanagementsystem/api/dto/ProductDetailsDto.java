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
public class ProductDetailsDto {

    Long productId;

    Long quantity;

    @Override
    public String toString() {
        return "ProductDetailsDto{" +
                "numberOfProduct=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
