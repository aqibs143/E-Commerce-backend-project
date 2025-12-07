
package salesSavvy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import salesSavvy.entity.Cart;
import salesSavvy.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    
}