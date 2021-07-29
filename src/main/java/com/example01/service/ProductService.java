package com.example01.service;

import com.example01.entity.Product;
import com.example01.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> findAll(){ return productRepository.findAll(); }
    public List<Product> findByStatus(Boolean status){ return productRepository.findByStatus(status); }
    public Product findById(String id){ return productRepository.findById(id).orElse(null); }
    public Product save(Product product){ return productRepository.save(product); }
    public Product edit(Product product){return productRepository.save(product);}
    public void delete(Product product) { productRepository.delete(product);}

    public Page<Product> paginate(String category, String name, Pageable pageable){
        return name.equals("null")
                ? (category.equals("null"))
                    ? productRepository.findAll(pageable)
                    : productRepository.findByCategory(category, pageable)
                : (category.equals("null"))
                    ? productRepository.findByNameLike(name, pageable)
                    : productRepository.findByNameLikeAndCategory(name, category, pageable);
    }
}
