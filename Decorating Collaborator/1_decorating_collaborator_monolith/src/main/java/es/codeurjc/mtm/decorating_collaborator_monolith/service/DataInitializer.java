package es.codeurjc.mtm.decorating_collaborator_monolith.service;

import es.codeurjc.mtm.decorating_collaborator_monolith.model.Order;
import es.codeurjc.mtm.decorating_collaborator_monolith.model.Invoicing;
import es.codeurjc.mtm.decorating_collaborator_monolith.model.Payroll;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final OrderService orderService;

  public DataInitializer(
      OrderService orderService) {
    this.orderService = orderService;
  }

  @PostConstruct
  public void initializeData() {
    // Order
    Order order = Order.builder()
        .prize(1049.95)
        .description("Bicycle")
        .build();

    Order order2 = Order.builder()
        .prize(549.95)
        .description("Scooter")
        .build();

    Order order3 = Order.builder()
        .prize(4549.95)
        .description("Motorcycle")
        .build();

    orderService.saveInventory(order);
    orderService.saveInventory(order2);
    orderService.saveInventory(order3);

  }
}
