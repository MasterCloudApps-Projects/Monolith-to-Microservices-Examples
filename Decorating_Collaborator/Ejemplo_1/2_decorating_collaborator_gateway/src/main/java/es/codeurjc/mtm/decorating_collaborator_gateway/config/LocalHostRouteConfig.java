package es.codeurjc.mtm.decorating_collaborator_gateway.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import es.codeurjc.mtm.decorating_collaborator_gateway.orders.OrderHandlers;
import es.codeurjc.mtm.decorating_collaborator_gateway.proxies.LoyaltyServiceProxy;
import es.codeurjc.mtm.decorating_collaborator_gateway.proxies.OrderServiceProxy;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class LocalHostRouteConfig {

  @Bean
  public RouteLocator localHostRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(r -> r.path("/order/**")
            .filters(f -> f.rewritePath("/order/(?<segment>.*)", "/order/${segment}"))
            .uri("http://localhost:8080"))
        .route(r -> r.path("/loyalty/**")
            .filters(f -> f
                .rewritePath("/loyalty/(?<segment>.*)", "/loyalty/${segment}"))
            .uri("http://localhost:8081"))
        .build();
  }

  @Bean
  public RouterFunction<ServerResponse> orderHandlerRouting(OrderHandlers orderHandlers) {
    return
        RouterFunctions
            .route(POST("/order"), orderHandlers::addLoyaltyDetails);
  }

  @Bean
  public OrderHandlers orderHandlers(OrderServiceProxy orderService, LoyaltyServiceProxy loyaltyService) {
    return new OrderHandlers(orderService, loyaltyService);
  }

  @Bean
  public WebClient webClient() {
    return WebClient.create();
  }

}
