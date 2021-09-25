package es.codeurjc.mtm.parallel_run_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payroll {

  private long id;

  private String shipTo;

  private Double total;

}
