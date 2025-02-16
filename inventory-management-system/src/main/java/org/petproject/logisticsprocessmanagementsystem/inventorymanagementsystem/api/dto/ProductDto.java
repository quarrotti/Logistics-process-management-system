package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @NonNull
    Long id;

    @NonNull
    String name;

    long quantity;
}
