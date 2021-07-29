package com.example01.repository;

import com.example01.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

    Page<Order> findByStatus(String status, Pageable pageable);
    Page<Order> findByCustomerLike(String customer, Pageable pageable);
    Page<Order> findByCustomerLikeAndStatus(String customer, String status ,Pageable pageable);

}
