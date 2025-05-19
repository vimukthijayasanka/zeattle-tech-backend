package lk.ijse.dep13.zeattle_tech.service.user;

import lk.ijse.dep13.zeattle_tech.dto.UserDTO;
import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.request.CreateUserRequest;
import lk.ijse.dep13.zeattle_tech.request.UserUpdateRequest;

public interface UserService {
    UserDTO getUserById(Long userId);

    User getEntityUserById(Long userId);

    UserDTO createUser(CreateUserRequest request);
    UserDTO updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);
}
