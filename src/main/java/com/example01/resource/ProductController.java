package com.example01.resource;

import com.example01.dto.PaginateResult;
import com.example01.entity.Product;
import com.example01.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<?>findAll(){ return ResponseEntity.ok(productService.findAll()); }

    @GetMapping("/status")
    public ResponseEntity<?> findByStatus(@RequestParam(defaultValue = "true") Boolean status){ return ResponseEntity.ok(productService.findByStatus(status)); }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        if(id == null) { return ResponseEntity.badRequest().body("Need to specify the ID"); }
        Product product = productService.findById(id);
        if (product == null) { return ResponseEntity.badRequest().body("Product not found, try another id."); }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Product product){
        try {
            Product newProduct = productService.save(product);
            return ResponseEntity.status(201).body(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> edit(@RequestBody Product product){
        if(product.getId() == null){ return ResponseEntity.badRequest().body("Need to specify the ID"); }
        Product currentProduct = productService.findById(product.getId());
        if (currentProduct == null){ return ResponseEntity.badRequest().body("Product not found"); }
        Product saveProduct = productService.edit(product);
        return ResponseEntity.ok(saveProduct);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody Product product){
        if(product.getId() == null) { return ResponseEntity.badRequest().body("Need to specify the ID"); }
        Product currentProduct = productService.findById(product.getId());
        if (currentProduct == null) { return ResponseEntity.ok("No product found.");}
        productService.delete(currentProduct);
        return ResponseEntity.ok("Product successfully deleted");
    }

    @GetMapping("/paginate")
    public ResponseEntity<?>paginate(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") Integer orderBy,
            @RequestParam(defaultValue = "null") String category,
            @RequestParam(defaultValue = "null") String name
    ){
        Pageable pageable = orderBy == 1 ? PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()) :
                            ( orderBy == 2
                                        ? PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending())
                                        : PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
                            );
        Page<Product> productPage = productService.paginate(category, name,pageable);
        int totalItems = productService.findAll().size();
        return ResponseEntity.ok(new PaginateResult(totalItems, productPage));
    }

}
