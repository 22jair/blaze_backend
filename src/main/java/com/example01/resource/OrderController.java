package com.example01.resource;

import com.example01.dto.PaginateResult;
import com.example01.entity.Order;
import com.example01.entity.Product;
import com.example01.entity.ProductOrder;
import com.example01.service.OrderService;
import com.example01.service.ProductOrderService;
import com.example01.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductOrderService productOrderService;

    @GetMapping
    public ResponseEntity<?> findAll(){ return ResponseEntity.ok(orderService.findAll()); }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        if(id == null) { return ResponseEntity.badRequest().body("Need to specify the ID"); }
        Order order = orderService.findById(id);
        if (order == null) { return ResponseEntity.badRequest().body("Product not found, try another id."); }
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Order order){
        try {
            double subTotal = 0;

            if(order.getProductOrderList() == null || order.getProductOrderList().size() == 0) throw new Exception("Insert at least 1 product!");

            for (ProductOrder productOrder : order.getProductOrderList()) {
                ProductOrder currentProductOrder = productOrder;
                Product product = productService.findById(currentProductOrder.getProduct().getId());
                if (product == null) throw new Exception("Order Product N°: " + currentProductOrder.getProductOrderNumber() + " does not exits!");
                double cost = currentProductOrder.getQuantity()*product.getUnitPrice();
                productOrder.setUnitPrice(product.getUnitPrice());
                productOrder.setCost(cost);
                subTotal += cost;
                productOrderService.save(currentProductOrder);
            };

            order.setSubTotal(subTotal);
            order.calculateTaxes();
            order.setTotalAmounts(subTotal + order.getTotalTaxes());
            order.setStatus("Pending");
            order.setOrderNumber(orderService.findAll().size()+1);

            Order newOrder = orderService.save(order);

            return ResponseEntity.status(201).body(newOrder);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Order order){
        Order currentOrder = orderService.findById(order.getId());
        if (currentOrder == null) return ResponseEntity.badRequest().body("Order Id not found or not Exist");

        double subTotal = 0;

        if(order.getProductOrderList() == null || order.getProductOrderList().size() == 0)  return ResponseEntity.badRequest().body("Insert at least 1 product!");
        for (ProductOrder productOrder : order.getProductOrderList()) {
            ProductOrder currentProductOrder = productOrder;
            Product product = productService.findById(currentProductOrder.getProduct().getId());
            double cost = currentProductOrder.getQuantity()*product.getUnitPrice();
            productOrder.setUnitPrice(product.getUnitPrice());
            productOrder.setCost(cost);
            subTotal += cost;
            productOrderService.save(currentProductOrder);
        };
        currentOrder.setSubTotal(subTotal);
        currentOrder.calculateTaxes();

        orderService.save(currentOrder);
        return ResponseEntity.ok(currentOrder);
    }

    @PutMapping("/status")
    public ResponseEntity<?> changeStatus(@RequestBody Order order){
        Order currentOrder = orderService.findById(order.getId());
        if (currentOrder == null) return ResponseEntity.badRequest().body("Order Id not found or not Exist");
        currentOrder.setStatus(order.getStatus());
        return ResponseEntity.ok(orderService.edit(currentOrder));
    }

    @DeleteMapping("/item")
    public ResponseEntity<?> deleteItem(
            @RequestParam(defaultValue = "null") String idOrder,
            @RequestParam(defaultValue = "null") String idProductOrder
    ){
        Order currentOrder = orderService.findById(idOrder);
        if (currentOrder == null) return ResponseEntity.badRequest().body("Order Id not found or not Exist");

        ProductOrder currentProductOrder = productOrderService.findById(idProductOrder);
        if (currentProductOrder == null) return ResponseEntity.badRequest().body("Item Id not found or not Exist");

        currentOrder.setSubTotal(currentOrder.getSubTotal() - currentProductOrder.getCost());
        currentOrder.calculateTaxes();
        orderService.save(currentOrder);
        productOrderService.delete(currentProductOrder);

        return ResponseEntity.ok("Item successfully deleted");
    }

    @PostMapping("/item")
    public ResponseEntity<?> addItem(@RequestBody Order order){
        Order currentOrder = orderService.findById(order.getId());
        if (currentOrder == null) return ResponseEntity.badRequest().body("Order Id not found or not Exist");

        List<ProductOrder> productOrderList = order.getProductOrderList();
        ProductOrder newProductOrder = productOrderList.get(0);

        double cost = newProductOrder.getQuantity() * newProductOrder.getProduct().getUnitPrice();
        newProductOrder.setUnitPrice(newProductOrder.getProduct().getUnitPrice());
        newProductOrder.setCost(cost);

        currentOrder.setSubTotal(currentOrder.getSubTotal() + cost);
        currentOrder.calculateTaxes();

        List<ProductOrder> currentProductOrderList = currentOrder.getProductOrderList();
        currentProductOrderList.add(newProductOrder);
        currentOrder.setProductOrderList(currentProductOrderList);


        newProductOrder.setProductOrderNumber(currentOrder.getProductOrderList().size()+1);
        productOrderService.save(newProductOrder);

        orderService.save(currentOrder);

        return ResponseEntity.status(201).body("Item added successfully ");
    }

    @GetMapping("/paginate")
    public ResponseEntity<?>paginate(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") Integer orderBy,
            @RequestParam(defaultValue = "null") String customer,
            @RequestParam(defaultValue = "null") String status
    ){
        Pageable pageable = orderBy == 1 ? PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()) :
                ( orderBy == 2
                        ? PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending())
                        : PageRequest.of(pageNumber, pageSize, Sort.by(sortBy))
                );

        Page<Order> orderPage = orderService.paginate(customer, status ,pageable);
        int totalItems = orderService.findAll().size();
        return ResponseEntity.ok(new PaginateResult(totalItems, orderPage));
    }
}
