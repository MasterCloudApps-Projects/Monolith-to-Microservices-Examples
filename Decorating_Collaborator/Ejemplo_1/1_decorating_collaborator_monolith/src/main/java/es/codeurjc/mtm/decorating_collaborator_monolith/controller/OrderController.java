package es.codeurjc.mtm.decorating_collaborator_monolith.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.codeurjc.mtm.decorating_collaborator_monolith.model.Order;
import es.codeurjc.mtm.decorating_collaborator_monolith.service.OrderService;
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
@RequestMapping("/order")
public class OrderController {

  private OrderService orderService;

  public OrderController(
      OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping({""})
  public ResponseEntity<Order> createInventory(@RequestBody Order order) {
    this.orderService.saveInventory(order);
    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();

    return ResponseEntity.created(location).body(order);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getInventory(
      @PathVariable long id) {
    Order order = this.orderService.getInventory(id);

    if (order != null) {
      return ResponseEntity.ok(this.orderService.getInventory(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping({""})
  public ResponseEntity<Collection<Order>> getInventories() {
    return ResponseEntity.ok(orderService.findAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Order> deleteInventory(
      @PathVariable long id) {
    Order order = this.orderService.getInventory(id);

    if (order != null) {
      this.orderService.deleteInventory(id);
      return ResponseEntity.ok(order);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<Order> createInventory(@RequestBody Order order,
      @PathVariable long id) {
    Order orderUpdated = this.orderService.updateInventory(id, order);

    if (orderUpdated != null) {
      return ResponseEntity.ok(order);
    } else {
      return ResponseEntity.notFound().build();
    }

  }

}
