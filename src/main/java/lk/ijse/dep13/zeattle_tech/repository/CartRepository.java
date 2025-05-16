package lk.ijse.dep13.zeattle_tech.repository;

import lk.ijse.dep13.zeattle_tech.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUserId(Long userId);
}
