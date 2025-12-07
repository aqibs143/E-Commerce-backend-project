package salesSavvy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import salesSavvy.entity.OrderItem;
import salesSavvy.entity.UserOrder;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    boolean existsByProductId(Long productId);

    void deleteByProductId(Long productId);

    /**
     * Nullify the product relation for all OrderItem rows that reference the given product id.
     * IMPORTANT: JPQL uses entity field names — `oi.product` must match the field name in OrderItem.
     */
    @Modifying
    @Transactional
    @Query("UPDATE OrderItem oi SET oi.product = NULL WHERE oi.product.id = :productId")
    void nullifyProductReference(@Param("productId") Long productId);

	List<OrderItem> findByUserOrder(UserOrder o);
}
