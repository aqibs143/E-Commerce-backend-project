package salesSavvy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salesSavvy.dto.CartData;
import salesSavvy.dto.CartItemDTO;

import salesSavvy.service.CartService;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
public class CartController {
    @Autowired 
    private CartService cartService;

    @PostMapping("/addToCart")
    public String addToCart(@RequestBody CartData data) {
        cartService.addToCart(data);
        return "success";
    }

    @PostMapping("/updateCartItem")
    public String updateCartItem(@RequestBody CartData data) {
        cartService.updateCartItem(data);
        return "success";
    }

    @GetMapping("/viewCart")
    public List<CartItemDTO> viewCart(@RequestParam String username) {
        return cartService.getCartItems(username);
    }
    
    @DeleteMapping("/cart/remove")
    public ResponseEntity<?> removeItem(@RequestParam String username, 
            @RequestParam Long productId) {
    	cartService.deleteCartItem(username, productId);
    	return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearCart(@RequestParam String username) {
    	cartService.clearCart(username);
    	return ResponseEntity.ok("Cart cleared");
    }  
}