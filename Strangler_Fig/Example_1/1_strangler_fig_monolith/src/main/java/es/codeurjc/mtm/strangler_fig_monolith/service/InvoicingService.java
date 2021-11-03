package es.codeurjc.mtm.strangler_fig_monolith.service;

import es.codeurjc.mtm.strangler_fig_monolith.model.Invoicing;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class InvoicingService {

  private final ConcurrentMap<Long, Invoicing> invoicings = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();
  private UserNotificationService userNotificationService;

  public InvoicingService(
      UserNotificationService userNotificationService) {
    this.userNotificationService = userNotificationService;
  }

  public Collection<Invoicing> findAll() {
    return invoicings.values();
  }

  public void saveInvoicing(Invoicing invoicing) {
    long id = nextId.getAndIncrement();
    invoicing.setId(id);
    this.invoicings.put(id, invoicing);

    userNotificationService.notify(String.format("Invoicing billed to %s of %s", invoicing.getBillTo(),
    invoicing.getTotal()));
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
