package com.example01.service;

import com.example01.entity.Product;
import com.example01.entity.ProductOrder;
import com.example01.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductOrderService {

    @Autowired
    ProductOrderRepository productOrderRepository;

    public ProductOrder save(ProductOrder ProductOrder){ return productOrderRepository.save(ProductOrder); }
    public ProductOrder findById(String id){ return productOrderRepository.findById(id).orElse(null); }
    public void delete(ProductOrder productOrder) { productOrderRepository.delete(productOrder);}
}
