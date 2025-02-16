package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.repositories;

import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
}
