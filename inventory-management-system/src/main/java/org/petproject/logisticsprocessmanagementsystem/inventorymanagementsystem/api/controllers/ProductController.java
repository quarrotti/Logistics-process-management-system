package org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.controllers;

import lombok.RequiredArgsConstructor;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.dto.ProductDto;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.api.services.ProductService;
import org.petproject.logisticsprocessmanagementsystem.inventorymanagementsystem.store.entities.ProductEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("")
    public ResponseEntity<?> findAll(){
        List<ProductDto> responseList = new ArrayList<>();

        for(ProductEntity entity : productService.findAll()){
            responseList.add(ProductDto.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .quantity(entity.getQuantity()).build());
        }
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){

        ProductEntity entity = productService.findById(id);

        if(entity == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Продукта с таким id не существует.");
        }
        return ResponseEntity.ok(ProductDto.builder()
                                .id(entity.getId())
                                .name(entity.getName())
                                .quantity(entity.getQuantity()).build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductDto dto){
        if(productService.findById(dto.getId()) != null){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                    .body("Продукт с таким id уже сущетсвует. Повторите попытку с другим id.");
        }

        if(dto.getQuantity() < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Количество продукта не может быть меньше 0.");
        }

        productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Продукт создан!");
    }

    @PatchMapping("/{id}/add-quantity")
    public ResponseEntity<?> addQuantity(@PathVariable Long id, @RequestParam Long quantity){

        ProductEntity entity = productService.findById(id);

        if(entity == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Продукта с таким id не существует.");
        }

        if(quantity <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Количество добавленного продукта не может быть 0 или меньше.");
        }

        productService.addQuantity(entity, quantity);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Продукт добавлен!");

    }
}
