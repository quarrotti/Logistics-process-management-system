package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.repositories;

import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities.ProductTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionRepository extends JpaRepository<ProductTransactionEntity, Long> {

}

