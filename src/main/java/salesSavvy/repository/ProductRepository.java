package salesSavvy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import salesSavvy.entity.Product;

public interface ProductRepository
						extends JpaRepository<Product, Long> {
	// This interface will handle CRUD operations for Product entities
	// Additional custom query methods can be defined here if needed
	List<Product> findByNameContainingIgnoreCase(String name);

}
