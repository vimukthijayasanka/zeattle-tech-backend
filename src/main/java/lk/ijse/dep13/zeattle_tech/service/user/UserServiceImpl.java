package lk.ijse.dep13.zeattle_tech.service.user;

import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.exception.AlreadyExistsException;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.repository.UserRepository;
import lk.ijse.dep13.zeattle_tech.request.CreateUserRequest;
import lk.ijse.dep13.zeattle_tech.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setPassword(request.getPassword());
                    user.setEmail(request.getEmail());
                    return userRepository.save(user);
                }).orElseThrow(()-> new AlreadyExistsException(request.getEmail() + "already exists!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User with id " + userId + " not found");
        });
    }
}
