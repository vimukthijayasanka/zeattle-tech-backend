package lk.ijse.dep13.zeattle_tech.repository;

import lk.ijse.dep13.zeattle_tech.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
