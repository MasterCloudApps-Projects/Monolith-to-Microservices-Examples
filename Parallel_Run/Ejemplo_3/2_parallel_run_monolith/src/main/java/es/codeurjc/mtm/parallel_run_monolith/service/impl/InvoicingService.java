package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import static es.codeurjc.mtm.parallel_run_monolith.config.FeatureFlagsInitializer.FEATURE_USER_NOTIFICATION_MS;

import es.codeurjc.mtm.parallel_run_monolith.model.Invoicing;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class InvoicingService {

  private final ConcurrentMap<Long, Invoicing> invoicings = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  private UserNotificationService userNotificationService;
  private UserNotificationService userNotificationServiceMS;
  private FF4j ff4j;

  public InvoicingService(
      @Qualifier("userNotificationServiceImpl") UserNotificationService userNotificationService,
      @Qualifier("userNotificationServiceMSImpl") UserNotificationService userNotificationServiceMS,
      FF4j ff4j) {
    this.userNotificationService = userNotificationService;
    this.userNotificationServiceMS = userNotificationServiceMS;
    this.ff4j = ff4j;
  }

  public Collection<Invoicing> findAll() {
    return invoicings.values();
  }

  public void saveInvoicing(Invoicing invoicing) {
    long id = nextId.getAndIncrement();
    invoicing.setId(id);
    this.invoicings.put(id, invoicing);

    if (ff4j.check(FEATURE_USER_NOTIFICATION_MS)) {
      userNotificationServiceMS.notify(
          String.format("Payroll %s billed to %s of %s", invoicing.getId(), invoicing.getBillTo(),
              invoicing.getTotal()));
    } else {
      userNotificationService.notify(
          String.format("Payroll %s billed to %s of %s", invoicing.getId(), invoicing.getBillTo(),
              invoicing.getTotal()));
    }

  }

  public Invoicing getInvoicing(Long id) {
    return this.invoicings.get(id);
  }

  public Invoicing deleteInvoicing(Long id) {
    return this.invoicings.remove(id);
  }

  public Invoicing updateInvoicing(long id, Invoicing invoicing) {
    Invoicing invoicingStoraged = this.invoicings.get(id);
    if (invoicingStoraged == null) {
      return null;
    } else {
      invoicingStoraged.setBillTo(invoicing.getBillTo());
      invoicingStoraged.setTotal(invoicing.getTotal());

      return invoicingStoraged;
    }
  }
}
