package lk.ijse.dep13.zeattle_tech.service.order;

import lk.ijse.dep13.zeattle_tech.dto.OrderDTO;
import lk.ijse.dep13.zeattle_tech.entity.Cart;
import lk.ijse.dep13.zeattle_tech.entity.Order;
import lk.ijse.dep13.zeattle_tech.entity.OrderItem;
import lk.ijse.dep13.zeattle_tech.entity.Product;
import lk.ijse.dep13.zeattle_tech.enums.OrderStatus;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.repository.OrderRepository;
import lk.ijse.dep13.zeattle_tech.repository.ProductRepository;
import lk.ijse.dep13.zeattle_tech.service.cart.CartService;
import lk.ijse.dep13.zeattle_tech.service.util.Transformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final Transformer transformer;

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

    @Override
    public Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());
        return order;
    }

    @Override
    public List<OrderItem> createOrderItems(Order order, Cart cart) {
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

    @Override
    public BigDecimal calculateTotalAmount(List<OrderItem> orderItemsList) {
        return orderItemsList.stream().map(item ->
                         item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(transformer::orderToOrderDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        return orderList.stream()
                .map(transformer::orderToOrderDTO)
                .toList();
    }
}
