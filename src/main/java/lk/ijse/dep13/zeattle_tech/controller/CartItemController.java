package lk.ijse.dep13.zeattle_tech.controller;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import lk.ijse.dep13.zeattle_tech.dto.UserDTO;
import lk.ijse.dep13.zeattle_tech.entity.Cart;
import lk.ijse.dep13.zeattle_tech.entity.User;
import lk.ijse.dep13.zeattle_tech.exception.ResourceNotFoundException;
import lk.ijse.dep13.zeattle_tech.response.ApiResponse;
import lk.ijse.dep13.zeattle_tech.service.cart.CartItemService;
import lk.ijse.dep13.zeattle_tech.service.cart.CartService;
import lk.ijse.dep13.zeattle_tech.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            User userById = userService.getAuthenticatedUser();
            Cart cart = cartService.initializeNewCart(userById);
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add item success", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,
                                                          @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove Item success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
