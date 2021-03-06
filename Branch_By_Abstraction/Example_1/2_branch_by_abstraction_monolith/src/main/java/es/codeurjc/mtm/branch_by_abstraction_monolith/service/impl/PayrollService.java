package es.codeurjc.mtm.branch_by_abstraction_monolith.service.impl;

import static es.codeurjc.mtm.branch_by_abstraction_monolith.config.FeatureFlagsInitializer.FEATURE_USER_NOTIFICATION_MS;

import es.codeurjc.mtm.branch_by_abstraction_monolith.model.Payroll;
import es.codeurjc.mtm.branch_by_abstraction_monolith.service.UserNotificationService;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

  private final ConcurrentMap<Long, Payroll> payrolls = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  private UserNotificationService userNotificationService;
  private UserNotificationService userNotificationServiceMS;
  private FF4j ff4j;

  public PayrollService(
      @Qualifier("userNotificationServiceImpl") UserNotificationService userNotificationService,
      @Qualifier("userNotificationServiceMSImpl") UserNotificationService userNotificationServiceMS,
      FF4j ff4j) {
    this.userNotificationService = userNotificationService;
    this.userNotificationServiceMS = userNotificationServiceMS;
    this.ff4j = ff4j;

  }

  public Collection<Payroll> findAll() {
    return payrolls.values();
  }

  public void savePayroll(Payroll payroll) {
    long id = nextId.getAndIncrement();
    payroll.setId(id);
    this.payrolls.put(id, payroll);

    if (ff4j.check(FEATURE_USER_NOTIFICATION_MS)) {
      userNotificationServiceMS.notify(String.format("Payroll %s shipped to %s of %s", payroll.getId(), payroll.getShipTo(),
          payroll.getTotal()));
    } else {
      userNotificationService.notify(String.format("Payroll %s shipped to %s of %s", payroll.getId(), payroll.getShipTo(),
          payroll.getTotal()));
    }


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
