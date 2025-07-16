package com.dhanush.productservice.controller;

import com.dhanush.productservice.DTO.ProductRequest;
import com.dhanush.productservice.DTO.ProductResponse;
import com.dhanush.productservice.model.Product;
import com.dhanush.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create-product")
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productrequest){
        productService.createProduct(productrequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
