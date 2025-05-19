package lk.ijse.dep13.zeattle_tech.controller;

import lk.ijse.dep13.zeattle_tech.dto.UserDTO;
import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.exception.AlreadyExistsException;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.request.CreateUserRequest;
import lk.ijse.dep13.zeattle_tech.request.UserUpdateRequest;
import lk.ijse.dep13.zeattle_tech.response.ApiResponse;
import lk.ijse.dep13.zeattle_tech.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            UserDTO user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("success", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            System.out.println(request);
            UserDTO user = userService.createUser(request);
            System.out.println(user);
            return ResponseEntity.ok(new ApiResponse("Create user account success", user));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId){
        try {
            UserDTO user = userService.updateUser(request, userId);
            return ResponseEntity.ok(new ApiResponse("Update user account success", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete user success", null));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
