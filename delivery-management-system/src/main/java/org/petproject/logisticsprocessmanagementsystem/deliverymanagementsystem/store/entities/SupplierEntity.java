package org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "supplier")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SupplierEntity {
    @Id
    Long id;

    @Enumerated(EnumType.STRING)
    SupplierStatus status;

    Long orderId;

}
