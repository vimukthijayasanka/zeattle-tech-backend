package lk.ijse.dep13.zeattle_tech.data;

import lk.ijse.dep13.zeattle_tech.entity.Role;
import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.repository.RoleRepository;
import lk.ijse.dep13.zeattle_tech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER");
        createDefaultUserIfNotExists();
        createDefaultRolesIfNotExists(defaultRoles);
        createDefaultAdminIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        Role userRole = roleRepository.findByName("ROLE_CUSTOMER").get();
        for (int i = 0; i < 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default user " + i + " created successfully");
        }
    }

    private void createDefaultAdminIfNotExists() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        String defaultEmail = "admin@gmail.com";
        if (userRepository.existsByEmail(defaultEmail)) {
            return;
        }
        User user = new User();
        user.setFirstName("Admin");
        user.setLastName("admin 1");
        user.setEmail(defaultEmail);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRoles(Set.of(adminRole));
        userRepository.save(user);
        System.out.println("Default admin " + " created successfully");
    }

    private void createDefaultRolesIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }
}
