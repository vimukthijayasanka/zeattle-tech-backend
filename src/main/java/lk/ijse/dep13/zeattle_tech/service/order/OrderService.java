package lk.ijse.dep13.zeattle_tech.service.order;

import lk.ijse.dep13.zeattle_tech.dto.OrderDTO;
import lk.ijse.dep13.zeattle_tech.entity.Cart;
import lk.ijse.dep13.zeattle_tech.entity.Order;
import lk.ijse.dep13.zeattle_tech.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);

    Order createOrder(Cart cart);

    List<OrderItem> createOrderItems(Order order, Cart cart);

    BigDecimal calculateTotalAmount(List<OrderItem> orderItemsList);

    OrderDTO getOrder(Long orderId);

    List<OrderDTO> getUserOrders(Long userId);
}
