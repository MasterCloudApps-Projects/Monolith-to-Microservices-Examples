package es.codeurjc.mtm.change_data_capture_monolith.service;

import es.codeurjc.mtm.change_data_capture_monolith.model.Loyalty;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final LoyaltyService loyaltyService;

  public DataInitializer(
      LoyaltyService loyaltyService
  ) {
    this.loyaltyService = loyaltyService;
  }

  @PostConstruct
  public void initializeData() {
    // Loyalty
    Loyalty loyalty = Loyalty.builder()
        .customerId(1L)
        .loyaltyAccount("1234-1567")
        .build();

    Loyalty loyalty2 = Loyalty.builder()
        .customerId(2L)
        .loyaltyAccount("2234-1567")
        .build();

    Loyalty loyalty3 = Loyalty.builder()
        .customerId(3L)
        .loyaltyAccount("3234-1567")
        .build();

    loyaltyService.saveLoyalty(loyalty);
    loyaltyService.saveLoyalty(loyalty2);
    loyaltyService.saveLoyalty(loyalty3);

  }
}
