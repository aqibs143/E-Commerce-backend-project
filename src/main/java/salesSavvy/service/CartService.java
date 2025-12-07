package salesSavvy.service;

import salesSavvy.dto.CartData;
import salesSavvy.dto.CartItemDTO;

import java.util.List;

	public interface CartService {
		void addToCart(CartData data);
	    void updateCartItem(CartData data);
		List<CartItemDTO> getCartItems(String username);
		void clearCart(String username);
		void deleteCartItem(String username, Long productId);
}
