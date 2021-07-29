package com.example01.repository;

import com.example01.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByStatus(Boolean status);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByNameLike(String name, Pageable pageable);
    Page<Product> findByNameLikeAndCategory(String name, String category ,Pageable pageable);
}
