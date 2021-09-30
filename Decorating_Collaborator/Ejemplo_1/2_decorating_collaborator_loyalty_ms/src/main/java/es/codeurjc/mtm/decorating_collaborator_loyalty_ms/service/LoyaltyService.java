package es.codeurjc.mtm.decorating_collaborator_loyalty_ms.service;

import static es.codeurjc.mtm.decorating_collaborator_loyalty_ms.util.Constants.INCREMENTAL_POINTS;

import es.codeurjc.mtm.decorating_collaborator_loyalty_ms.model.Loyalty;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {

  private final ConcurrentMap<Long, Loyalty> orders = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  public LoyaltyService() {
  }

  public Collection<Loyalty> findAll() {
    return orders.values();
  }

  public Loyalty saveLoyalty(String username) {
    Map.Entry<Long, Loyalty> loyaltyStoraged = this.getLoyaltyMapByUserName(username);

    if (loyaltyStoraged != null) {
      loyaltyStoraged.getValue()
          .setPoints(loyaltyStoraged.getValue().getPoints() + INCREMENTAL_POINTS);

      return loyaltyStoraged.getValue();

    } else {
      long id = nextId.getAndIncrement();
      Loyalty loyalty = Loyalty.builder().id(id).points(INCREMENTAL_POINTS).userName(username).build();
      this.orders.put(id, loyalty);

      return loyalty;

    }
  }

  public Loyalty getLoyalty(Long id) {
    return this.orders.get(id);
  }

  public Loyalty getLoyaltyByUserName(String username) {

    return this.getLoyaltyMapByUserName(username)!=null?this.getLoyaltyMapByUserName(username).getValue():null;

  }

  public Map.Entry<Long, Loyalty> getLoyaltyMapByUserName(String username) {
    for (Map.Entry<Long, Loyalty> entry : orders.entrySet()) {
      if (entry.getValue().getUserName().equals(username)) {
        return entry;
      }
    }
    return null;

  }

  public Loyalty deleteLoyalty(Long id) {
    return this.orders.remove(id);
  }

  public Loyalty updateLoyalty(long id, Loyalty loyalty) {
    Loyalty loyaltyStoraged = this.orders.get(id);
    if (loyaltyStoraged == null) {
      return null;
    } else {
      loyaltyStoraged.setUserName(loyalty.getUserName());
      loyaltyStoraged.setPoints(loyalty.getPoints());

      return loyaltyStoraged;
    }
  }
}
