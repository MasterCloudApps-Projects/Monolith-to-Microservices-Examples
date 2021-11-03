package es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies;

import lombok.Data;

@Data
public class OrderInfo {

  private long id;

  private String description;

  private Double prize;

  private String userName;

}