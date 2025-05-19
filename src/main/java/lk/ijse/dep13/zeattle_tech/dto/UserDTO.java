package lk.ijse.dep13.zeattle_tech.dto;

import lk.ijse.dep13.zeattle_tech.entity.Cart;
import lk.ijse.dep13.zeattle_tech.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDTO> orders;
    private CartDTO cart;
}
