package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Table(name = "product")
@Entity
@Getter
@Setter
public class ProductEntity {
    @Id
    Long id;

    String name;

    long quantity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    List<ProductTransactionEntity> productTransactions;

}
