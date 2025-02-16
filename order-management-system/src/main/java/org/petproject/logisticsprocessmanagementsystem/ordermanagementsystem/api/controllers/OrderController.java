package org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.controllers;

import lombok.RequiredArgsConstructor;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.dto.OrderRequestDto;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.dto.OrderResponseDto;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.api.services.OrderService;
import org.petproject.logisticsprocessmanagementsystem.ordermanagementsystem.store.entities.OrderEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity<?> findAll(){
        List<OrderResponseDto> responseList = new ArrayList<>();

        for(OrderEntity entity : orderService.findAll()){
            responseList.add(OrderResponseDto.builder()
                    .id(entity.getId())
                    .created_at(entity.getCreated_at())
                    .description(entity.getDescription())
                    .executionDetails(entity.getExecutionDetails())
                    .status(entity.getStatus().name()).build());
        }
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        OrderEntity order = orderService.findById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Заказа с таким id не существует.");
        }
        return ResponseEntity.ok(OrderResponseDto.builder()
                .id(order.getId())
                .created_at(order.getCreated_at())
                .description(order.getDescription())
                .executionDetails(order.getExecutionDetails())
                .status(order.getStatus().name()).build());
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto dto){
        if(dto.getProductDetails().getQuantity() <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Количество продукта не может быть 0 или меньше");
        } else if (dto.getProductDetails().getProductId() <= 0 ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Идентификатор продукта не может быть 0 или меньше");
        }
        else{
            orderService.createOrder(dto);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Заказ создан.");
        }
    }


}
