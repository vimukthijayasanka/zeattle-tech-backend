package lk.ijse.dep13.zeattle_tech.service.user;

import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.request.CreateUserRequest;
import lk.ijse.dep13.zeattle_tech.request.UserUpdateRequest;

public interface UserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);
}
