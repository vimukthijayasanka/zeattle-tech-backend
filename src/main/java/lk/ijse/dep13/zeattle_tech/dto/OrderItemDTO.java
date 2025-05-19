package lk.ijse.dep13.zeattle_tech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
