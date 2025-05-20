package lk.ijse.dep13.zeattle_tech.service.user;

import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.repository.UserRepository;
import lk.ijse.dep13.zeattle_tech.security.user.ShopUserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
        return ShopUserDetailsImpl.buildUserDetails(user);
    }
}
