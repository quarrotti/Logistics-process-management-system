package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.services;

import lombok.RequiredArgsConstructor;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.dto.ProductDto;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities.ProductEntity;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities.ProductTransactionEntity;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities.ProductTransactionType;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.repositories.ProductRepository;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.repositories.ProductTransactionRepository;
import org.petproject.logisticsprocessmanagementsystem.kafkacore.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTransactionRepository productTransactionRepository;
    private final KafkaTemplate<String, OrderEvent> template;

    public void save(ProductEntity product){
        productRepository.save(product);
    }

    public ProductEntity findById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    public List<ProductEntity> findAll(){
        return productRepository.findAll();
    }

    public void createProduct(ProductDto dto){
        ProductEntity entity = new ProductEntity();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setQuantity(dto.getQuantity());

        productRepository.save(entity);
    }

    public void addQuantity(ProductEntity entity, Long quantity){

        entity.setQuantity(entity.getQuantity() + quantity);
        productRepository.save(entity);

        ProductTransactionEntity productTransaction = new ProductTransactionEntity();
        productTransaction.setProduct(entity);
        productTransaction.setQuantity(quantity);
        productTransaction.setType(ProductTransactionType.Income);
        productTransactionRepository.save(productTransaction);

    }
    @KafkaListener(topics = "oi-order-created-event-topic", groupId = "inventory-group")
    public void orderProcessing(OrderEvent orderEvent){
        ProductEntity product = findById(orderEvent.getProductDetailsEvent().getProductId());
        if(product == null){
            //todo ответ об отсутствии товара
            orderEvent.setInternal_execution_code(1);
            template.send("io-order-created-event-topic", null, orderEvent);
        } else if (product.getQuantity() < orderEvent.getProductDetailsEvent().getQuantity()){
            //todo ответ о нехватке товара
            orderEvent.setInternal_execution_code(2);
            template.send("io-order-created-event-topic", null, orderEvent);
        } else{
            ProductTransactionEntity productTransaction = new ProductTransactionEntity();
            productTransaction.setProduct(product);
            productTransaction.setQuantity(orderEvent.getProductDetailsEvent().getQuantity());
            productTransaction.setType(ProductTransactionType.Expense);
            productTransaction.setOrderId(orderEvent.getOrderId());
            productTransactionRepository.save(productTransaction);

            product.setQuantity(product.getQuantity() - productTransaction.getQuantity());
            save(product);
            //todo ответ об одобрении товара и ожидания доставки
            orderEvent.setInternal_execution_code(4);
            template.send("io-order-created-event-topic", null, orderEvent);
            //todo запрос в сервис доставки
            template.send("id-order-created-event-topic", null, orderEvent);
        }
    }
    @KafkaListener(topics = "do-order-fail-created-event-topic", groupId = "inventory-group")
    public void failDelivery(OrderEvent orderEvent){
        ProductTransactionEntity productTransaction = new ProductTransactionEntity();
        productTransaction.setOrderId(orderEvent.getOrderId());
        productTransaction.setQuantity(orderEvent.getProductDetailsEvent().getQuantity());
        productTransaction.setType(ProductTransactionType.Expense);
        productTransaction.setProduct(findById(orderEvent.getProductDetailsEvent().getProductId()));

        productTransactionRepository.save(productTransaction);

        ProductEntity product = findById(orderEvent.getProductDetailsEvent().getProductId());
        product.setQuantity(product.getQuantity() + orderEvent.getProductDetailsEvent().getQuantity());
        save(product);
    }


}
