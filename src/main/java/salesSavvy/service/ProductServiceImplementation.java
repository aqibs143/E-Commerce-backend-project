package salesSavvy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import salesSavvy.entity.Product;
import salesSavvy.repository.CartItemRepository;
import salesSavvy.repository.OrderItemRepository;
import salesSavvy.repository.ProductRepository;

@Service
public class ProductServiceImplementation implements ProductService {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Override
    public Product addProduct(Product prod) {
        // Ensure basic invariants if needed (example)
        if (prod == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Product cannot be null");
        }
        return repo.save(prod);
    }

    @Override
    public Product updateProduct(Product prod) {
        if (prod == null || prod.getId() == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Product or id missing for update");
        }

        if (!repo.existsById(prod.getId())) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Product not found");
        }

        return repo.save(prod);
    }

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public List<Product> searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return repo.findByNameContainingIgnoreCase(query.trim());
    }

    /**
     * Delete product safely:
     * - Nullify product references in order_items and cart_item (so FK constraints don't block)
     * - Delete product row
     *
     * Returns true if deletion happened, false if product not found.
     */
    @Override
    @Transactional
    public boolean deleteProduct(Long productId) {
        if (productId == null) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Product id is required");
        }

        if (!repo.existsById(productId)) {
            return false; // caller/controller will translate to 404
        }

        // Nullify references in order_items and cart_item — method names depend on your repo implementations.
        // Ensure these repository methods are implemented (e.g., @Modifying @Query in repo).
        try {
            orderItemRepo.nullifyProductReference(productId);
        } catch (Exception ex) {
            // Log or rethrow if needed; keep transaction consistent
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to clear order references before deleting product", ex);
        }

        try {
            cartItemRepo.nullifyProductReference(productId);
        } catch (Exception ex) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to clear cart references before deleting product", ex);
        }

        repo.deleteById(productId);
        return true;
    }
}
