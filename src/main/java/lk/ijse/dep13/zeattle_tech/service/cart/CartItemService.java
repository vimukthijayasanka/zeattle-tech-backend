package lk.ijse.dep13.zeattle_tech.service.cart;

import lk.ijse.dep13.zeattle_tech.entity.CartItem;

public interface CartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long productId);
}
