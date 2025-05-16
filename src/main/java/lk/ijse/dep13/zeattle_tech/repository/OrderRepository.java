package lk.ijse.dep13.zeattle_tech.repository;

import lk.ijse.dep13.zeattle_tech.entity.Order;
import lk.ijse.dep13.zeattle_tech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> user(User user);
    List<Order> findByUserId(Long userId);
}
