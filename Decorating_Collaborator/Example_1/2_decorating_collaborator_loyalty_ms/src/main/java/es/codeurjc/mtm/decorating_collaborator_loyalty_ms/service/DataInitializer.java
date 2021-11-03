package es.codeurjc.mtm.decorating_collaborator_loyalty_ms.service;

import es.codeurjc.mtm.decorating_collaborator_loyalty_ms.model.Loyalty;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final LoyaltyService loyaltyService;

  public DataInitializer(
      LoyaltyService loyaltyService) {
    this.loyaltyService = loyaltyService;
  }

  @PostConstruct
  public void initializeData() {
    // Loyalty
    loyaltyService.saveLoyalty("Juablaz1");
    loyaltyService.saveLoyalty("Juablaz2");
    loyaltyService.saveLoyalty("Juablaz3");

  }
}
