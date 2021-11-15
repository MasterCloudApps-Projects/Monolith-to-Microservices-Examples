package es.codeurjc.mtm.parallel_run_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inventory {

  private long id;

  private String description;

  private Double prize;

}
