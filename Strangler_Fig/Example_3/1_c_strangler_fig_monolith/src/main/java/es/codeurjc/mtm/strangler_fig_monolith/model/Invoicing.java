package es.codeurjc.mtm.strangler_fig_monolith.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class Invoicing {

  private long id;

  private String billTo;

  private Double total;

  public Invoicing() {
  }

  public Invoicing(long id, String billTo, Double total) {
    this.id = id;
    this.billTo = billTo;
    this.total = total;
  }
}
