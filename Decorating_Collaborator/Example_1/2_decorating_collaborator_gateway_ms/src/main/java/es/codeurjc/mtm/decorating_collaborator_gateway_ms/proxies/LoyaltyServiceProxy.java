package es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies;

import es.codeurjc.mtm.decorating_collaborator_gateway_ms.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LoyaltyServiceProxy {

  private final WebClient client;

  @Value("${loyalty.host}")
  private String LOYALTY_HOST;

  @Value("${loyalty.port}")
  private int LOYALTY_PORT;

  @Autowired
  public LoyaltyServiceProxy(WebClient client) {
    this.client = client;
  }

  public Mono<LoyaltyInfo> createOrUpdate(String userName) {
    Mono<ClientResponse> response = client.post()
        .uri("http://" + LOYALTY_HOST + ":" + LOYALTY_PORT + "/loyalty/" + userName).exchange();

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
