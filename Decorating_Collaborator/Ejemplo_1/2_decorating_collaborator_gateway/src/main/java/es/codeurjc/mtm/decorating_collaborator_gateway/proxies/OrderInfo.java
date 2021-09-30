package es.codeurjc.mtm.decorating_collaborator_gateway.proxies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

  private long id;

  private String description;

  private Double prize;

  private String userName;

}