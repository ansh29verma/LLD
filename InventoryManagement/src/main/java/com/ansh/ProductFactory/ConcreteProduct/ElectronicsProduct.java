package com.ansh.ProductFactory.ConcreteProduct;


import com.ansh.ProductFactory.Product;
import com.ansh.ProductFactory.ProductCategory;
import lombok.Data;

@Data
public class ElectronicsProduct extends Product {
    private String brand;
    private int warrantyPeriod; // in months

    public ElectronicsProduct(String sku, String name, double price, int quantity, int threshold) {
        super();
        setSku(sku);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setProductCategory(ProductCategory.ELECTRONICS);
        setThreshold(threshold);
    }
}
