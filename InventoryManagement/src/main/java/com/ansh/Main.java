package com.ansh;

import com.ansh.InventoryStockManager.InventoryManager;
import com.ansh.ProductFactory.Product;
import com.ansh.ProductFactory.ProductCategory;
import com.ansh.ProductFactory.ProductFactory;
import com.ansh.ReplinshmentsStrategy.ConcreteReplenish.BulkOrderStrategy;
import com.ansh.ReplinshmentsStrategy.ConcreteReplenish.JustInTimeStrategy;
import com.ansh.ReplinshmentsStrategy.ReplenishmentStrategy;
import com.ansh.Utility.Warehouse;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ReplenishmentStrategy replenishmentStrategy = new JustInTimeStrategy();
        InventoryManager inventoryManager = InventoryManager.getInstance(replenishmentStrategy);

        // Create and add warehouses
        Warehouse warehouse1 = new Warehouse("Warehouse 1");
        Warehouse warehouse2 = new Warehouse("Warehouse 2");
        inventoryManager.addWarehouse(warehouse1);
        inventoryManager.addWarehouse(warehouse2);

        // Create products using ProductFactory
        ProductFactory productFactory = new ProductFactory();
        Product laptop = productFactory.createProduct(
                ProductCategory.ELECTRONICS, "SKU123", "Laptop", 1000.0, 50, 25);
        Product tShirt = productFactory.createProduct(
                ProductCategory.CLOTHING, "SKU456", "T-Shirt", 20.0, 200, 100);
        Product apple = productFactory.createProduct(
                ProductCategory.GROCERY, "SKU789", "Apple", 1.0, 100, 200);

        // Add products to warehouses
        warehouse1.addProduct(laptop, 15);
        warehouse1.addProduct(tShirt, 20);
        warehouse2.addProduct(apple, 50);

        // Set replenishment strategy to Just-In-Time
        inventoryManager.setReplenishmentStrategy(new JustInTimeStrategy());

        // Perform inventory check and replenish if needed
        inventoryManager.performInventoryCheck();

        // Switch replenishment strategy to Bulk Order
        inventoryManager.setReplenishmentStrategy(new BulkOrderStrategy());

        // Replenish a specific product if needed
        inventoryManager.checkAndReplenish("SKU123");

    }
}