package es.codeurjc.mtm.change_data_capture_monolith.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Loyalty {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private long customerId;

  private String loyaltyAccount;

}
