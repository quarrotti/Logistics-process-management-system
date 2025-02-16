package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "product_transaction")
@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDateTime created_at;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    ProductEntity product;

    Long quantity;

    Long orderId;

    @Enumerated(EnumType.STRING)
    ProductTransactionType type;

    @PrePersist
    public void init(){
        created_at = LocalDateTime.now();
    }

}
