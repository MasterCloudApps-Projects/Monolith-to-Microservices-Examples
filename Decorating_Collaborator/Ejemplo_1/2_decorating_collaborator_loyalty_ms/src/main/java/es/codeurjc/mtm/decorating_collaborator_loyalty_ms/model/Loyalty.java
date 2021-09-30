package es.codeurjc.mtm.decorating_collaborator_loyalty_ms.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Loyalty {

  private long id;

  private String userName;

  private Double points;
}
