package es.codeurjc.mtm.decorating_collaborator_gateway.proxies;

import es.codeurjc.mtm.decorating_collaborator_gateway.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LoyaltyServiceProxy {

  private final WebClient client;

  @Value("#{'${delivery.url:http://localhost:8081}'}")
  private String loyaltyServiceUrl;

  @Autowired
  public LoyaltyServiceProxy(WebClient client) {
    this.client = client;
  }

  public Mono<LoyaltyInfo> findLoyaltyByUserName(String userName) {

    Mono<ClientResponse> response = client.post()
        .uri(loyaltyServiceUrl + "/loyalty/"+userName)
        .exchange();

    return response.flatMap(resp -> {
      switch (resp.statusCode()) {
        case CREATED:
        case OK:
          return resp.bodyToMono(LoyaltyInfo.class);
        case NOT_FOUND:
          return Mono.error(new EntityNotFoundException());
        default:
          return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
      }
    });

  }

  public Mono<LoyaltyInfo> createOrUpdate(String userName) {
    Mono<ClientResponse> response = client.post()
        .uri(loyaltyServiceUrl + "/loyalty/"+ userName).exchange();

    return response.flatMap(resp -> {
      switch (resp.statusCode()) {
        case CREATED:
        case OK:
          return resp.bodyToMono(LoyaltyInfo.class);
        case NOT_FOUND:
          return Mono.error(new EntityNotFoundException());
        default:
          return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
      }
    });

  }

}
