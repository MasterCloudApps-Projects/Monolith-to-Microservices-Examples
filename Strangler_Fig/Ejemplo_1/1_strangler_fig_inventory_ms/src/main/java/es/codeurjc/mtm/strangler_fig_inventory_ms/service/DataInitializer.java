package es.codeurjc.mtm.strangler_fig_inventory_ms.service;

import es.codeurjc.mtm.strangler_fig_inventory_ms.model.Inventory;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final InventoryService inventoryService;

  public DataInitializer(
      InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostConstruct
  public void initializeData() {
    // Inventory
    Inventory inventory = Inventory.builder()
        .prize(1049.95)
        .description("[MS] Bicycle")
        .build();

    Inventory inventory2 = Inventory.builder()
        .prize(549.95)
        .description("[MS] Scooter")
        .build();

    Inventory inventory3 = Inventory.builder()
        .prize(4549.95)
        .description("[MS] Motorcycle")
        .build();

    inventoryService.saveInventory(inventory);
    inventoryService.saveInventory(inventory2);
    inventoryService.saveInventory(inventory3);
  }
}
