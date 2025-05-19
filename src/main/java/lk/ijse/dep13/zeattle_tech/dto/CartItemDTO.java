package lk.ijse.dep13.zeattle_tech.dto;

import lk.ijse.dep13.zeattle_tech.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO implements Serializable {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDTO product;
}
