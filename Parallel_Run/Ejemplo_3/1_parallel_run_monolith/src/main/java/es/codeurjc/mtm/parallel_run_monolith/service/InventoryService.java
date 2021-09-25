package es.codeurjc.mtm.parallel_run_monolith.service;

import es.codeurjc.mtm.parallel_run_monolith.model.Inventory;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

  private final ConcurrentMap<Long, Inventory> inventorys = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  public InventoryService() {
  }

  public Collection<Inventory> findAll() {
    return inventorys.values();
  }

  public void saveInventory(Inventory inventory) {
    long id = nextId.getAndIncrement();
    inventory.setId(id);
    this.inventorys.put(id, inventory);
  }

  public Inventory getInventory(Long id) {
    return this.inventorys.get(id);
  }

  public Inventory deleteInventory(Long id) {
    return this.inventorys.remove(id);
  }

  public Inventory updateInventory(long id, Inventory inventory) {
    Inventory inventoryStoraged = this.inventorys.get(id);
    if (inventoryStoraged == null) {
      return null;
    } else {
      inventoryStoraged.setDescription(inventory.getDescription());
      inventoryStoraged.setPrize(inventory.getPrize());

      return inventoryStoraged;
    }
  }
}
