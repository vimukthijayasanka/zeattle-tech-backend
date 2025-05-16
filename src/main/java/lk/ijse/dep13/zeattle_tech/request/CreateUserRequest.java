package lk.ijse.dep13.zeattle_tech.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
