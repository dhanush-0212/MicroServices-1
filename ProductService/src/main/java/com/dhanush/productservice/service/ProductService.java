package com.dhanush.productservice.service;

import com.dhanush.productservice.DTO.ProductRequest;
import com.dhanush.productservice.DTO.ProductResponse;
import com.dhanush.productservice.model.Product;
import com.dhanush.productservice.repository.Productrepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final Productrepo productrepo;
    private final ModelMapper modelMapper;

    public void createProduct(ProductRequest productrequest) {
        Product product = Product.builder()
                .name(productrequest.getName())
                .description(productrequest.getDescription())
                .price(productrequest.getPrice())
                .build();
        productrepo.save(product);

    log.info("Product created id : {}", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productrepo.findAll();
        return products.stream()
                        .map(product ->modelMapper.map(product,ProductResponse.class))
                        .toList();
    }

}
