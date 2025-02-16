package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.repositories;

import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Override
    Optional<OrderEntity> findById(Long id);

    @Override
    List<OrderEntity> findAll();
}
