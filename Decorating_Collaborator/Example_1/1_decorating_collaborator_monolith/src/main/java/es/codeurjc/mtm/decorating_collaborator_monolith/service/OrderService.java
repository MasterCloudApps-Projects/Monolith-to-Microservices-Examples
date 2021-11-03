package es.codeurjc.mtm.decorating_collaborator_monolith.service;

import es.codeurjc.mtm.decorating_collaborator_monolith.model.Order;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final ConcurrentMap<Long, Order> orders = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  public OrderService() {
  }

  public Collection<Order> findAll() {
    return orders.values();
  }

  public void saveOrder(Order order) {
    long id = nextId.getAndIncrement();
    order.setId(id);
    this.orders.put(id, order);
  }

  public Order getOrder(Long id) {
    return this.orders.get(id);
  }

  public Order deleteOrder(Long id) {
    return this.orders.remove(id);
  }

  public Order updateOrder(long id, Order order) {
    Order orderStoraged = this.orders.get(id);
    if (orderStoraged == null) {
      return null;
    } else {
      orderStoraged.setDescription(order.getDescription());
      orderStoraged.setPrize(order.getPrize());

      return orderStoraged;
    }
  }
}
