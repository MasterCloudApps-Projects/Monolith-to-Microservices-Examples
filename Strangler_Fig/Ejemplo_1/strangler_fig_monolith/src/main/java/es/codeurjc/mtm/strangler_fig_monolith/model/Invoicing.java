package es.codeurjc.mtm.strangler_fig_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Invoicing {

  private long id;

  private String billTo;

  private Double total;

}
