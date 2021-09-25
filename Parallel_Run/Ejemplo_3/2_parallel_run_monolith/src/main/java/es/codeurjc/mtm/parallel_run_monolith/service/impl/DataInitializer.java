package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Invoicing;
import es.codeurjc.mtm.parallel_run_monolith.model.Payroll;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final InvoicingService invoicingService;
  private final PayrollService payrollService;

  public DataInitializer(
      InvoicingService invoicingService,
      PayrollService payrollService) {
    this.invoicingService = invoicingService;
    this.payrollService = payrollService;
  }

  @PostConstruct
  public void initializeData() {
    // Invoicing
    Invoicing invoicing = Invoicing.builder()
        .billTo("[Canary] user 1")
        .total(4549.95)
        .build();

    Invoicing invoicing2 = Invoicing.builder()
        .billTo("[Canary] user 2")
        .total(549.95)
        .build();

    Invoicing invoicing3 = Invoicing.builder()
        .billTo("[Canary] user 3")
        .total(1049.95)
        .build();

    invoicingService.saveInvoicing(invoicing);
    invoicingService.saveInvoicing(invoicing2);
    invoicingService.saveInvoicing(invoicing3);

    // Payroll
    Payroll payroll = Payroll.builder()
        .shipTo("[Canary] user 1")
        .total(10549.95)
        .build();

    Payroll payroll2 = Payroll.builder()
        .shipTo("[Canary] user 2")
        .total(6549.95)
        .build();

    Payroll payroll3 = Payroll.builder()
        .shipTo("[Canary] user 3")
        .total(1449.95)
        .build();

    payrollService.savePayroll(payroll);
    payrollService.savePayroll(payroll2);
    payrollService.savePayroll(payroll3);
  }
}
