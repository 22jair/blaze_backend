package com.example01.entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "productOrder")
public class ProductOrder {

    @Id
    private String id;

    @NotNull
    @Min(0)
    private Integer productOrderNumber;

    @NotNull
    @Min(0)
    private Integer quantity;

    private Double unitPrice;

    private Double cost;

    @DBRef
    private Product product;

    @PersistenceConstructor
    public ProductOrder(@NotNull @Min(0) Integer productOrderNumber, @NotNull @Min(0) Integer quantity, Double unitPrice ,Double cost, Product product) {
        this.productOrderNumber = productOrderNumber;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.cost = cost;
        this.product = product;
    }
}
