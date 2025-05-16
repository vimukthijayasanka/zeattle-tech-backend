package lk.ijse.dep13.zeattle_tech.repository;

import lk.ijse.dep13.zeattle_tech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
