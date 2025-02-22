package org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.api.services;

import lombok.RequiredArgsConstructor;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.entities.SupplierEntity;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.entities.SupplierStatus;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.repositories.SupplierRepository;
import org.petproject.logisticsprocessmanagementsystem.kafkacore.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final KafkaTemplate<String, OrderEvent> template;
    private Timer timer = new Timer();

    public void save(SupplierEntity supplier){
        supplierRepository.save(supplier);
    }

    public List<SupplierEntity> findAll(){
        return supplierRepository.findAll();
    }

    public SupplierEntity findById(Long id){
        return supplierRepository.findById(id).orElse(null);
    }
    public List<SupplierEntity> findByStatus(SupplierStatus status){
        return supplierRepository.findAllByStatus(status);
    }

    public void createSupplier(Long id){
        SupplierEntity entity = new SupplierEntity();
        entity.setId(id);
        entity.setStatus(SupplierStatus.Free);

        supplierRepository.save(entity);
    }
    @KafkaListener(topics = "id-order-created-event-topic", groupId = "delivery-group")
    public void inventoryRequestProcessing(OrderEvent orderEvent) {
        final int maxRetries = 10; 
        final int waitTime = 10000; 
        int attempts = 0;

        while (attempts < maxRetries) {
            if (!findByStatus(SupplierStatus.Free).isEmpty()) {
                SupplierEntity supplier = findByStatus(SupplierStatus.Free).get(0);
                supplier.setOrderId(orderEvent.getOrderId());
                supplier.setStatus(SupplierStatus.Busy);
                supplierRepository.save(supplier);

                orderEvent.setSupplierId(supplier.getId());
                orderEvent.setInternal_execution_code(5);
                template.send("do-order-created-event-topic", null, orderEvent);
                return;
            } else {
                attempts++;
                try {
                    Thread.sleep(waitTime); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); 
                    
                    break;
                }
            }
        }
        orderEvent.setInternal_execution_code(3);
        template.send("do-order-created-event-topic", null, orderEvent);
        template.send("di-order-fail-created-event-topic", null, orderEvent);
    }
    public boolean deliveryComplete(Long supplierId, Long orderId) {
        SupplierEntity supplier = findById(supplierId);
        if(supplier != null && supplier.getOrderId() == orderId) {
            supplier.setStatus(SupplierStatus.Free);
            supplier.setOrderId(null);
            save(supplier);


            OrderEvent orderEvent = new OrderEvent();
            orderEvent.setOrderId(orderId);
            orderEvent.setInternal_execution_code(6);

            template.send("do-order-complete-event-topic", null, orderEvent);

            return true;

        } else return false;
    }


}
