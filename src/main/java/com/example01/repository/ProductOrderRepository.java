package com.example01.repository;

import com.example01.entity.ProductOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductOrderRepository extends MongoRepository<ProductOrder, String> {

}
