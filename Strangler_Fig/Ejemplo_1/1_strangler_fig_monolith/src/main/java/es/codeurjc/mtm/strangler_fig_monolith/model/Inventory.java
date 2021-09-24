package es.codeurjc.mtm.strangler_fig_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inventory {

  private long id;

  private String description;

  private Double prize;

}
