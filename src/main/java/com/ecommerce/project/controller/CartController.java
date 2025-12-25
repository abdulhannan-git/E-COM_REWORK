package com.ecommerce.project.controller;

import com.ecommerce.project.dto.CartItemRequest;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        if (cartService.addItem(userId, cartItemRequest)) {
            return new ResponseEntity<>("Item is added to the cart successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product NOT FOUND OR User is NOT FOUND OR The Product is OUT OF STOCK!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-USER-ID") String userId
    ) {
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteFromCart(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long productId
    ) {
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
