package salesSavvy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import salesSavvy.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProdId(Long cartId, Long prodId);

    void deleteByProd_Id(Long id);

    /**
     * Nullify the product reference in cart items for the given product id.
     * Adjust `c.prod` if your CartItem entity uses a different field name for the Product relation.
     */
    @Modifying
    @Transactional
    @Query("UPDATE CartItem c SET c.prod = NULL WHERE c.prod.id = :pid")
    void nullifyProductReference(@Param("pid") Long pid);
}
