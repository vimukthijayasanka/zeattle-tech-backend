package lk.ijse.dep13.zeattle_tech.dto;

import lk.ijse.dep13.zeattle_tech.entity.OrderItem;
import lk.ijse.dep13.zeattle_tech.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {
    private Long userId;
    private Long orderId;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemDTO> orderItems;
}
