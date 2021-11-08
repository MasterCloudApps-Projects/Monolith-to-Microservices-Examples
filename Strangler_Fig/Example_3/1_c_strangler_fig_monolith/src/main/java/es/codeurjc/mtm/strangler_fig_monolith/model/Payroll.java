package es.codeurjc.mtm.strangler_fig_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payroll {

  private long id;

  private String shipTo;

  private Double total;

  public Payroll() {
  }

  public Payroll(long id, String shipTo, Double total) {
    this.id = id;
    this.shipTo = shipTo;
    this.total = total;
  }
}
