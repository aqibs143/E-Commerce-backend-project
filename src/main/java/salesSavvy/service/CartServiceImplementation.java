package salesSavvy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salesSavvy.dto.CartData;
import salesSavvy.dto.CartItemDTO;
import salesSavvy.entity.Cart;
import salesSavvy.entity.CartItem;
import salesSavvy.entity.Product;
import salesSavvy.entity.User;
import salesSavvy.repository.CartItemRepository;
import salesSavvy.repository.CartRepository;
import salesSavvy.repository.ProductRepository;
import salesSavvy.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired private CartRepository cartRepo;
    @Autowired private CartItemRepository itemRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private ProductRepository prodRepo;

    @Transactional
    public void addToCart(CartData data) {
        User user = userRepo.findByUsername(data.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseGet(() -> cartRepo.save(new Cart(user)));

        Product prod = prodRepo.findById(data.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Integer requestedQty = data.getQuantity();
        int qty = (requestedQty == null || requestedQty <= 0) ? 1 : requestedQty;

        Optional<CartItem> existing = itemRepo.findByCartIdAndProdId(cart.getId(), prod.getId());

        CartItem item;
        if (existing.isPresent()) {
            // increment by 1 intentionally for add-to-cart button
            item = existing.get();
            item.setQuantity(item.getQuantity() + qty);
        } else {
            item = new CartItem(cart, prod, qty);
        }

        itemRepo.save(item);
    }

    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItems(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Cart> optCart = cartRepo.findByUser(user);
        if (optCart.isEmpty()) return new ArrayList<>();

        Cart cart = optCart.get();
        List<CartItemDTO> out = new ArrayList<>();

        for (CartItem ci : cart.getItemList()) {
            Product p = ci.getProd();

            if (p == null) {  // for deleted products
                out.add(new CartItemDTO(
                        null,
                        "DELETED PRODUCT",
                        "This product was removed",
                        0.0,
                        null,
                        ci.getQuantity()
                ));
                continue;
            }

            out.add(new CartItemDTO(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getPrice(),
                    p.getImage(),
                    ci.getQuantity()
            ));
        }
        return out;
    }

    @Transactional
    public void deleteCartItem(String username, Long productId) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        CartItem item = itemRepo.findByCartIdAndProdId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        itemRepo.delete(item);
    }

    @Transactional
    public void clearCart(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        itemRepo.deleteAll(cart.getItemList());
        cart.getItemList().clear();
    }

    @Transactional
	public void updateCartItem(CartData data) {
	    User user = userRepo.findByUsername(data.getUsername())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Cart cart = cartRepo.findByUser(user)
	            .orElseThrow(() -> new RuntimeException("Cart not found"));

	    CartItem item = itemRepo.findByCartIdAndProdId(cart.getId(), data.getProductId())
	            .orElseThrow(() -> new RuntimeException("CartItem not found"));

	    int newQty = data.getQuantity();

	    if (newQty <= 0) {
	        itemRepo.delete(item);   // REMOVE item
	    } else {
	        item.setQuantity(newQty); // SET quantity (NOT +)
	        itemRepo.save(item);
	    }
	}

}
