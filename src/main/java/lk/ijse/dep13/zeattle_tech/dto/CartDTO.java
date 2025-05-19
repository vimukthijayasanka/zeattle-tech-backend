package lk.ijse.dep13.zeattle_tech.dto;

import lk.ijse.dep13.zeattle_tech.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO implements Serializable {
    private Long id;
    private Set<CartItemDTO> items;
    private BigDecimal totalAmount;
}
