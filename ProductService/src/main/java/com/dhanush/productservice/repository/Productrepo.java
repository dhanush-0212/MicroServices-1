package com.dhanush.productservice.repository;

import com.dhanush.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Productrepo extends MongoRepository<Product, String> {
}
