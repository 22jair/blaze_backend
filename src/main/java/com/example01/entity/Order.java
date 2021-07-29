package com.example01.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection="order")
public class Order {

    @Id
    private String id;

    @NotNull
    @Min(0)
    private long orderNumber;

    @NotEmpty
    private String status;
    private LocalDate date = LocalDate.now();

    @NotEmpty
    private String customer;

    private Double subTotal;
    private Double totalTaxes;
    private Double totalAmounts;

    @DBRef
    private List<ProductOrder> productOrderList;

    public void calculateTaxes(){
        double totalTaxes = 0;
        //Taxes
        double cityTax = this.subTotal * 0.1; // 10%
        totalTaxes += cityTax;
        double countryTax = (this.subTotal + totalTaxes ) * 0.05; // 5%
        totalTaxes += countryTax;
        double stateTax = (this.subTotal + totalTaxes ) * 0.08; // 8%
        totalTaxes += stateTax;
        double federalTax = (this.subTotal + totalTaxes ) * 0.02; // 2%
        totalTaxes += federalTax;
        this.setTotalTaxes(totalTaxes);
    }
}
