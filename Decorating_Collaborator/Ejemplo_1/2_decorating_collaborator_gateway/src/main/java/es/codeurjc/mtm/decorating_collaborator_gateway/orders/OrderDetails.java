package es.codeurjc.mtm.decorating_collaborator_gateway.orders;

import es.codeurjc.mtm.decorating_collaborator_gateway.proxies.LoyaltyInfo;
import es.codeurjc.mtm.decorating_collaborator_gateway.proxies.OrderInfo;
import lombok.Data;
import reactor.util.function.Tuple2;

@Data
public class OrderDetails {

  private OrderInfo orderInfo;
  private LoyaltyInfo loyaltyInfo;

  public OrderDetails(OrderInfo orderInfo, LoyaltyInfo loyaltyInfo) {
    this.orderInfo = orderInfo;
    this.loyaltyInfo = loyaltyInfo;
  }

  public static OrderDetails makeOrderDetails(
      Tuple2<OrderInfo, LoyaltyInfo> info) {
    return new OrderDetails(info.getT1(), info.getT2());
  }


}
