package lk.ijse.dep13.zeattle_tech.service.cart;

import lk.ijse.dep13.zeattle_tech.entity.Cart;
import lk.ijse.dep13.zeattle_tech.entity.User;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
}
