package es.codeurjc.mtm.branch_by_abstraction_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inventory {

  private long id;

  private String description;

  private Double prize;

}
