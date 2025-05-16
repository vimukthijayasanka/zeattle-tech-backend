package lk.ijse.dep13.zeattle_tech.service.cart;

import lk.ijse.dep13.zeattle_tech.entity.Cart;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Long initializeNewCart();
}
