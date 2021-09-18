package es.codeurjc.mtm.strangler_fig_payroll_ms.service;

import es.codeurjc.mtm.strangler_fig_payroll_ms.model.Payroll;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

  private final PayrollService payrollService;

  public DataInitializer(
      PayrollService payrollService) {
    this.payrollService = payrollService;
  }

  @PostConstruct
  public void initializeData() {

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
