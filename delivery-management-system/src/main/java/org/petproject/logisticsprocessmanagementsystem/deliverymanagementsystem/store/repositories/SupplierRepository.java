package org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.repositories;

import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.entities.SupplierEntity;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.entities.SupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, Long> {
    List<SupplierEntity> findAllByStatus(SupplierStatus status);
}
