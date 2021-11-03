package es.codeurjc.mtm.decorating_collaborator_gateway_ms.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import es.codeurjc.mtm.decorating_collaborator_gateway_ms.orders.OrderHandlers;
import es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies.LoyaltyServiceProxy;
import es.codeurjc.mtm.decorating_collaborator_gateway_ms.proxies.OrderServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class HostRouteConfig {
 
  @Value("${loyalty.host}")
  private String LOYALTY_HOST;

  @Value("${loyalty.port}")
  private int LOYALTY_PORT;

  @Value("${order.host}")
  private String ORDER_HOST;

  @Value("${order.port}")
  private int ORDER_PORT;

  @Bean
  public RouteLocator hostRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(r -> r.path("/order/**")
            .filters(f -> f.rewritePath("/order/(?<segment>.*)", "/order/${segment}"))
            .uri("http://" + ORDER_HOST + ":" + ORDER_PORT))
        .route(r -> r.path("/loyalty/**")
            .filters(f -> f
                .rewritePath("/loyalty/(?<segment>.*)", "/loyalty/${segment}"))
            .uri("http://" + LOYALTY_HOST + ":" + LOYALTY_PORT))
        .build();
  }

  @Bean
  public RouterFunction<ServerResponse> orderHandlerRouting(OrderHandlers orderHandlers) {
    return
        RouterFunctions
            .route(POST("/order"), orderHandlers::addLoyaltyDetails);
  }

  @Bean
  public OrderHandlers orderHandlers(OrderServiceProxy orderService,
      LoyaltyServiceProxy loyaltyService) {
    return new OrderHandlers(orderService, loyaltyService);
  }

  @Bean
  public WebClient webClient() {
    return WebClient.create();
  }

}
