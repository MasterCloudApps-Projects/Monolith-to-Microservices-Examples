package es.codeurjc.mtm.strangler_fig_payroll_ms.controller;

import es.codeurjc.mtm.strangler_fig_payroll_ms.model.Payroll;
import es.codeurjc.mtm.strangler_fig_payroll_ms.service.PayrollService;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

  private PayrollService payrollService;

  public PayrollController(
      PayrollService payrollService) {
    this.payrollService = payrollService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Payroll> getPayroll(
      @PathVariable long id) {
    Payroll payroll = this.payrollService.getPayroll(id);

    if (payroll != null) {
      return ResponseEntity.ok(this.payrollService.getPayroll(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping({""})
  public ResponseEntity<Collection<Payroll>> getPayrolls() {
    return ResponseEntity.ok(payrollService.findAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Payroll> deletePayroll(
      @PathVariable long id) {
    Payroll payroll = this.payrollService.getPayroll(id);

    if (payroll != null) {
      this.payrollService.deletePayroll(id);

      return ResponseEntity.ok(payroll);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<Payroll> createPayroll(@RequestBody Payroll payroll,
      @PathVariable long id) {
    Payroll payrollUpdated = this.payrollService.updatePayroll(id, payroll);

    if (payrollUpdated != null) {
      return ResponseEntity.ok(payroll);
    } else {
      return ResponseEntity.notFound().build();
    }

  }
}
