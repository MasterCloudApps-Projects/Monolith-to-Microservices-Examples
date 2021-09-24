package es.codeurjc.mtm.branch_by_abstraction_monolith.service;

import es.codeurjc.mtm.branch_by_abstraction_monolith.model.Inventory;
import es.codeurjc.mtm.branch_by_abstraction_monolith.model.Invoicing;
import es.codeurjc.mtm.branch_by_abstraction_monolith.model.Payroll;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final InventoryService inventoryService;
  private final InvoicingService invoicingService;
  private final PayrollService payrollService;

  public DataInitializer(
      InventoryService inventoryService,
      InvoicingService invoicingService,
      PayrollService payrollService) {
    this.inventoryService = inventoryService;
    this.invoicingService = invoicingService;
    this.payrollService = payrollService;
  }

  @PostConstruct
  public void initializeData() {
    // Inventory
    Inventory inventory = Inventory.builder()
        .prize(1049.95)
        .description("Bicycle")
        .build();

    Inventory inventory2 = Inventory.builder()
        .prize(549.95)
        .description("Scooter")
        .build();

    Inventory inventory3 = Inventory.builder()
        .prize(4549.95)
        .description("Motorcycle")
        .build();

    inventoryService.saveInventory(inventory);
    inventoryService.saveInventory(inventory2);
    inventoryService.saveInventory(inventory3);

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

    // Payroll
    Payroll payroll = Payroll.builder()
        .shipTo("user 1")
        .total(10549.95)
        .build();

    Payroll payroll2 = Payroll.builder()
        .shipTo("user 2")
        .total(6549.95)
        .build();

    Payroll payroll3 = Payroll.builder()
        .shipTo("user 3")
        .total(1449.95)
        .build();

    payrollService.savePayroll(payroll);
    payrollService.savePayroll(payroll2);
    payrollService.savePayroll(payroll3);
  }
}
