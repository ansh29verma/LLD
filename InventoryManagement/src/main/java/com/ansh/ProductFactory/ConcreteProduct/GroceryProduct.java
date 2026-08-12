package com.ansh.ProductFactory.ConcreteProduct;

import com.ansh.ProductFactory.Product;
import com.ansh.ProductFactory.ProductCategory;
import lombok.Data;

import java.util.Date;

@Data
public class GroceryProduct extends Product {
    private Date expiryDate;
    private boolean refrigerated;

    public GroceryProduct(String sku, String name, double price, int quantity, int threshold) {
        super();
        setSku(sku);
        setName(name);
        setPrice(price);
        setQuantity(quantity);
        setProductCategory(ProductCategory.GROCERY);
        setThreshold(threshold);
    }

}
