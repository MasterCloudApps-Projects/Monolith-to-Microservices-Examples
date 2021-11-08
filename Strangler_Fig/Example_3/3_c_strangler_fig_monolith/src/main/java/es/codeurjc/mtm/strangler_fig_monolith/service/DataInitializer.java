package es.codeurjc.mtm.strangler_fig_monolith.service;

import es.codeurjc.mtm.strangler_fig_monolith.model.Invoicing;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final InvoicingService invoicingService;

  public DataInitializer(
      InvoicingService invoicingService) {
    this.invoicingService = invoicingService;
  }

  @PostConstruct
  public void initializeData() {
    // Invoicing
    Invoicing invoicing = Invoicing.builder()
        .billTo("user 1")
        .total(4549.95)
        .build();

    Invoicing invoicing2 = Invoicing.builder()
        .billTo("user 2")
        .total(549.95)
        .build();

    Invoicing invoicing3 = Invoicing.builder()
        .billTo("user 3")
        .total(1049.95)
        .build();

    invoicingService.saveInvoicing(invoicing);
    invoicingService.saveInvoicing(invoicing2);
    invoicingService.saveInvoicing(invoicing3);

  }
}
