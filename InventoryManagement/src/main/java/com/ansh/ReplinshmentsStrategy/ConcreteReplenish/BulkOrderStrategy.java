package com.ansh.ReplinshmentsStrategy.ConcreteReplenish;

import com.ansh.ProductFactory.Product;
import com.ansh.ReplinshmentsStrategy.ReplenishmentStrategy;

public class BulkOrderStrategy implements ReplenishmentStrategy {
    @Override
    public void replenish(Product product) {
        // Implement Bulk Order replenishment logic
        System.out.println("Applying Bulk Order replenishment for " + product.getName());
        // Order in large quantities to minimize order costs
    }
}
