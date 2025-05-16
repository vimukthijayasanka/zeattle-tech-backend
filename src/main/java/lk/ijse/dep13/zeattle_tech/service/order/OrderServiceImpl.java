package lk.ijse.dep13.zeattle_tech.service.order;

import lk.ijse.dep13.zeattle_tech.entity.Cart;
import lk.ijse.dep13.zeattle_tech.entity.Order;
import lk.ijse.dep13.zeattle_tech.entity.OrderItem;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.enums.OrderStatus;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.repository.OrderRepository;
import lk.ijse.dep13.zeattle_tech.repository.ProductRepository;
import lk.ijse.dep13.zeattle_tech.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.ReadOnlyFileSystemException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order saveOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return saveOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(
                cartItem -> {
                    Product product = cartItem.getProduct();
                    // Reduce stock after making the order
                    product.setStock(product.getStock() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
                    );
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemsList) {
        return orderItemsList.stream().map(item ->
                         item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found"));
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
