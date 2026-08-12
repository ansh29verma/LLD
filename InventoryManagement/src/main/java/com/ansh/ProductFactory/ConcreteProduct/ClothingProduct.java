package com.ansh.ProductFactory.ConcreteProduct;

import com.ansh.ProductFactory.Product;
import com.ansh.ProductFactory.ProductCategory;
import lombok.Data;

@Data
public class ClothingProduct extends Product {
    private String size;
    private String color;

    public ClothingProduct(String sku, String name, double price, int quantity,int threshold) {
        super();
        setSku(sku);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setProductCategory(ProductCategory.CLOTHING);
        setThreshold(threshold);
    }
}
