package es.codeurjc.mtm.strangler_fig_monolith.service;

import es.codeurjc.mtm.strangler_fig_monolith.model.Inventory;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

  private final ConcurrentMap<Long, Inventory> inventories = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  public InventoryService() {
  }

  public Collection<Inventory> findAll() {
    return inventories.values();
  }

  public void saveInventory(Inventory inventory) {
    long id = nextId.getAndIncrement();
    inventory.setId(id);
    this.inventories.put(id, inventory);
  }

  public Inventory getInventory(Long id) {
    return this.inventories.get(id);
  }

  public Inventory deleteInventory(Long id) {
    return this.inventories.remove(id);
  }

  public Inventory updateInventory(long id, Inventory inventory) {
    Inventory inventoryStoraged = this.inventories.get(id);
    if (inventoryStoraged == null) {
      return null;
    } else {
      inventoryStoraged.setDescription(inventory.getDescription());
      inventoryStoraged.setPrize(inventory.getPrize());

      return inventoryStoraged;
    }
  }
}
