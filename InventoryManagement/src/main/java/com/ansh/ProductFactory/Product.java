package com.ansh.ProductFactory;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Product {
    private String sku;
    private String name;
    private double price;
    private int quantity;
    private int threshold;
    private ProductCategory productCategory;

}
