package org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.api.controllers;

import lombok.RequiredArgsConstructor;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.api.dto.SupplierDto;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.api.services.SupplierService;
import org.petproject.logisticsprocessmanagementsystem.deliverymanagementsystem.store.entities.SupplierEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("")
    public ResponseEntity<?> findAll(){
        List<SupplierDto> responseList = new ArrayList<>();

        for(SupplierEntity entity : supplierService.findAll()){
            responseList.add(SupplierDto.builder()
                    .id(entity.getId())
                    .status(entity.getStatus().name())
                    .orderId(entity.getOrderId()).build());
        }
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        SupplierEntity entity = supplierService.findById(id);

        if(entity == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Доставщика с таким id не существует.");
        }
        return ResponseEntity.ok(SupplierDto.builder()
                .id(entity.getId())
                .status(entity.getStatus().name())
                .orderId(entity.getOrderId()).build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestParam Long id){
        if(supplierService.findById(id) != null){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body("Доставщик с таким id уже сущетсвует. Повторите попытку с другим id.");
        }

        supplierService.createSupplier(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Доставщик создан!");
    }

    @PostMapping("/delivery-complete")
    public ResponseEntity<?> deliveryComplete(@RequestParam Long supplierId, @RequestParam Long orderId){
        if(supplierService.deliveryComplete(supplierId, orderId)){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Доставка закончена!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Введены неверные данные");
        }
    }
}
