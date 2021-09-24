package es.codeurjc.mtm.branch_by_abstraction_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payroll {

  private long id;

  private String shipTo;

  private Double total;

}
