package lk.ijse.dep13.zeattle_tech.service.order;

import lk.ijse.dep13.zeattle_tech.entity.Order;

public interface OrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);
}
