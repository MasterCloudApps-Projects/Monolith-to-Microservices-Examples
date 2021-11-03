package es.codeurjc.mtm.decorating_collaborator_gateway_ms.orders;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import es.codeurjc.mtm.decorating_collaborator_gateway_ms.exception.EntityNotFoundException;
import es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies.LoyaltyServiceProxy;
import es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies.OrderInfo;
import es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies.OrderServiceProxy;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class OrderHandlers {

  private final OrderServiceProxy orderService;
  private final LoyaltyServiceProxy loyaltyService;

  public OrderHandlers(OrderServiceProxy orderService, LoyaltyServiceProxy loyaltyService) {
    this.orderService = orderService;
    this.loyaltyService = loyaltyService;
  }

  public Mono<ServerResponse> addLoyaltyDetails(ServerRequest serverRequest) {

    Mono<OrderInfo> orderInfoMono = serverRequest.bodyToMono(OrderInfo.class);

    Mono<OrderInfo> orderInfo = orderService.createOrder(orderInfoMono);

    return orderInfo
        .zipWhen(orderInfo1 -> loyaltyService.createOrUpdate(orderInfo1.getUserName()))
        .flatMap(od -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
            .body(fromValue(od.getT1())))
        .onErrorResume(EntityNotFoundException.class, e -> ServerResponse.notFound().build());
  }
}
