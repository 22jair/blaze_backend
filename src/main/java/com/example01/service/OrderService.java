package com.example01.service;

import com.example01.entity.Order;
import com.example01.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<Order> findAll(){ return orderRepository.findAll(); }
    public Order findById(String id){ return orderRepository.findById(id).orElse(null); }
    public Order save(Order order){ return orderRepository.save(order); }
    public Order edit(Order order){return orderRepository.save(order);}

    public Page<Order> paginate(String customer, String status ,Pageable pageable){
        return customer.equals("null")
                ? (status.equals("null"))
                    ? orderRepository.findAll(pageable)
                    : orderRepository.findByStatus(status, pageable)
                : (status.equals("null"))
                    ? orderRepository.findByCustomerLike(customer, pageable)
                    : orderRepository.findByCustomerLikeAndStatus(customer, status, pageable);
    }
}
