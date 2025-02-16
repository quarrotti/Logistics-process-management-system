package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.repositories;

import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
