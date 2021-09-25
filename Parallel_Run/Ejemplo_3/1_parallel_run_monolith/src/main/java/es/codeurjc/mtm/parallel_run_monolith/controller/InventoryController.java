package es.codeurjc.mtm.parallel_run_monolith.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.codeurjc.mtm.parallel_run_monolith.model.Inventory;
import es.codeurjc.mtm.parallel_run_monolith.service.InventoryService;
import java.net.URI;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

  private InventoryService inventoryService;

  public InventoryController(
      InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping({""})
  public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
    this.inventoryService.saveInventory(inventory);
    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(inventory.getId()).toUri();

    return ResponseEntity.created(location).body(inventory);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Inventory> getInventory(
      @PathVariable long id) {
    Inventory inventory = this.inventoryService.getInventory(id);

    if (inventory != null) {
      return ResponseEntity.ok(this.inventoryService.getInventory(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping({""})
  public ResponseEntity<Collection<Inventory>> getInventories() {
    return ResponseEntity.ok(inventoryService.findAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Inventory> deleteInventory(
      @PathVariable long id) {
    Inventory inventory = this.inventoryService.getInventory(id);

    if (inventory != null) {
      this.inventoryService.deleteInventory(id);
      return ResponseEntity.ok(inventory);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory,
      @PathVariable long id) {
    Inventory inventoryUpdated = this.inventoryService.updateInventory(id, inventory);

    if (inventoryUpdated != null) {
      return ResponseEntity.ok(inventory);
    } else {
      return ResponseEntity.notFound().build();
    }

  }

}
