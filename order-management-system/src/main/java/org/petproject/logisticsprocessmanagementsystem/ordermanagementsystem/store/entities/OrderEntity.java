package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "app_order")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime created_at;

    String description;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    Long supplierId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    ProductDetails productDetails;

    String executionDetails;


    @PrePersist
    public void init(){
        created_at = LocalDateTime.now();
    }
}
