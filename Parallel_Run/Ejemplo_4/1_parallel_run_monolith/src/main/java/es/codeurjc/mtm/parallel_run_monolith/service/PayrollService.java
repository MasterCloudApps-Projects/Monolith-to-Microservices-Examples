package es.codeurjc.mtm.parallel_run_monolith.service;

import es.codeurjc.mtm.parallel_run_monolith.model.Payroll;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

  private final ConcurrentMap<Long, Payroll> payrolls = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();
  private UserNotificationService userNotificationService;

  public PayrollService(
      UserNotificationService userNotificationService) {
    this.userNotificationService = userNotificationService;
  }

  public Collection<Payroll> findAll() {
    return payrolls.values();
  }

  public void savePayroll(Payroll payroll) {
    long id = nextId.getAndIncrement();
    payroll.setId(id);
    this.payrolls.put(id, payroll);

    userNotificationService.notify(String.format("Payroll %s shipped to %s of %s", payroll.getId(), payroll.getShipTo(),
        payroll.getTotal()));
  }

  public Payroll getPayroll(Long id) {
    return this.payrolls.get(id);
  }

  public Payroll deletePayroll(Long id) {
    return this.payrolls.remove(id);
  }

  public Payroll updatePayroll(long id, Payroll payroll) {
    Payroll payrollStoraged = this.payrolls.get(id);
    if (payrollStoraged == null) {
      return null;
    } else {
      payrollStoraged.setShipTo(payroll.getShipTo());
      payrollStoraged.setTotal(payroll.getTotal());

      return payrollStoraged;
    }
  }
}
