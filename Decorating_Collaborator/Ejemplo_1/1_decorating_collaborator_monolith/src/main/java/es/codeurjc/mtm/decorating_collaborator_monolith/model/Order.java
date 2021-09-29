package es.codeurjc.mtm.decorating_collaborator_monolith.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

  private long id;

  private String description;

  private Double prize;

}
