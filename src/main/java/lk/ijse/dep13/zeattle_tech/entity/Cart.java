package lk.ijse.dep13.zeattle_tech.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<CartItem> cartItems;

    @OneToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalAmount();
    }

    public void removeItem(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            BigDecimal unitPrice = item.getUnitPrice();
            int quantity = item.getQuantity();

            if (unitPrice != null) {
                BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
                total = total.add(itemTotal);
            }
        }
        this.totalAmount = total;
    }
}
