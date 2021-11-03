package es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies;

import es.codeurjc.mtm.decorating_collaborator_gateway_ms.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceProxy {

  private final WebClient client;

  @Value("${order.host}")
  private String ORDER_HOST;

  @Value("${order.port}")
  private int ORDER_PORT;

  @Autowired
  public OrderServiceProxy(WebClient client) {
    this.client = client;
  }

  public Mono<OrderInfo> findOrderById(String orderId) {

    Mono<ClientResponse> response = client.get()
        .uri("http://" + ORDER_HOST + ":" + ORDER_PORT + "/order/{orderId}", orderId).exchange();

    return response.flatMap(resp -> {
      switch (resp.statusCode()) {
        case OK:
          return resp.bodyToMono(OrderInfo.class);
        case NOT_FOUND:
          return Mono.error(new EntityNotFoundException());
        default:
          return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
      }
    });
  }

  public Mono<OrderInfo> createOrder(Mono<OrderInfo> orderInfoMono) {

    Mono<ClientResponse> response = client.post()
        .uri("http://" + ORDER_HOST + ":" + ORDER_PORT + "/order")
        .body(orderInfoMono, OrderInfo.class).exchange();

    return response.flatMap(resp -> {
      switch (resp.statusCode()) {
        case CREATED:
        case OK:
          return resp.bodyToMono(OrderInfo.class);
        case NOT_FOUND:
          return Mono.error(new EntityNotFoundException());
        default:
          return Mono.error(new RuntimeException("Unknown" + resp.statusCode()));
      }
    });

  }
}
