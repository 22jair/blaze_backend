package com.example01.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "product")
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Please enter a name!")
    private String name;

    @NotBlank(message = "Please select a category!")
    private String category;

    @NotNull
    @Min(value = 0, message = "Price needs to be greater than $min")
    private Double unitPrice;

    @NotNull(message = "The product must have status!")
    private  Boolean status;

    @PersistenceConstructor
    public Product(String name, String category, Double unitPrice, Boolean status) {
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.status = status;
    }
}
