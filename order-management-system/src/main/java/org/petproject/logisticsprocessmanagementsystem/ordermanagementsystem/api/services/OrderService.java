package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.services;

import lombok.RequiredArgsConstructor;
import org.petproject.logisticsprocessmanagementsystem.kafkacore.OrderEvent;
import org.petproject.logisticsprocessmanagementsystem.kafkacore.ProductDetailsEvent;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.dto.OrderRequestDto;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities.OrderEntity;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities.OrderStatus;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities.ProductDetails;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.repositories.OrderRepository;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.repositories.ProductDetailsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaTemplate<String, OrderEvent> template;
    private final OrderRepository orderRepository;
    private final ProductDetailsRepository productDetailsRepository;

    public void save(OrderEntity order){
        orderRepository.save(order);
    }

    public OrderEntity findById(Long id){
        return orderRepository.findById(id).orElse(null);

    }

    public List<OrderEntity> findAll(){
        return orderRepository.findAll();
    }

    public boolean deleteById(Long id){ //look: использовать в случае нехватки ресурсов
        if(findById(id) != null){
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void createOrder(OrderRequestDto dto){

        OrderEntity order = new OrderEntity();
        order.setDescription(dto.getDescription());
        order.setStatus(OrderStatus.Received);
        save(order);

        ProductDetails productDetails = new ProductDetails();
        productDetails.setProductId(dto.getProductDetails().getProductId());
        productDetails.setQuantity(dto.getProductDetails().getQuantity());
        productDetails.setOrder(order);
        productDetailsRepository.save(productDetails);

        ProductDetailsEvent productDetailsEvent = new ProductDetailsEvent(productDetails.getProductId(),
                productDetails.getQuantity(),
                order.getId());

        OrderEvent orderEvent = new OrderEvent(order.getId(), productDetailsEvent);


        template.send("oi-order-created-event-topic", null, orderEvent);

    }
    @KafkaListener(topics = "io-order-created-event-topic", groupId = "order-management-group")
    public void inventoryResponseProcessing(OrderEvent orderEvent){
        OrderEntity order = findById(orderEvent.getOrderId());
        if(orderEvent.getInternal_execution_code() == 4){
            order.setStatus(OrderStatus.Approved);
            order.setExecutionDetails("ДЕТАЛИ ЗАКАЗА ПРИНЯТЫ. ПЕРЕДАЕМ В ДОСТАВКУ.");
            save(order);
        } else if(orderEvent.getInternal_execution_code() == 1){
            order.setStatus(OrderStatus.Rejected);
            order.setExecutionDetails("ТОВАРА С ВВЕДЕННЫМИ ИДЕНТИФИКАТОРОМ НЕ СУЩЕСТВУЕТ");
            save(order);
        } else if (orderEvent.getInternal_execution_code() == 2) {
            order.setStatus(OrderStatus.Rejected);
            order.setExecutionDetails("ЗАКАЗ БЫЛ ОТКЛОНЕН ИЗ-ЗА НЕХВАТКИ ТОВАРОВ НА СКЛАДЕ.");
            save(order);
        }
    }
    @KafkaListener(topics = "do-order-created-event-topic", groupId = "order-management-group")
    public void deliveryResponseProcessing(OrderEvent orderEvent){
        OrderEntity order = findById(orderEvent.getOrderId());
        if(orderEvent.getInternal_execution_code() == 3){
            order.setStatus(OrderStatus.Rejected);
            order.setExecutionDetails("ЗАКАЗ БЫЛ ОТКЛОНЕН ИЗ-ЗА ОТСУТСТВИЯ СВОБОДНЫХ КУРЬЕРОВ. ПОВТОРИТЕ ПОПЫТКУ ПОЗЖЕ.");
            save(order);
        } else if(orderEvent.getInternal_execution_code() == 5){
            order.setSupplierId(orderEvent.getSupplierId());
            order.setStatus(OrderStatus.Shipping);
            order.setExecutionDetails("ЗАКАЗ ПЕРЕДАН В ДОСТАВКУ. ОЖИДАЙТЕ....");
            save(order);
        }
    }
    @KafkaListener(topics = "do-order-complete-event-topic", groupId = "order-management-group")
    public void deliveryComplete(OrderEvent orderEvent){
        if(orderEvent.getInternal_execution_code() == 6){
            OrderEntity order = findById(orderEvent.getOrderId());
            order.setStatus(OrderStatus.Completed);
            order.setExecutionDetails("ЗАКАЗ ВЫПОЛНЕН!");
            orderRepository.save(order);
        }
    }

}
