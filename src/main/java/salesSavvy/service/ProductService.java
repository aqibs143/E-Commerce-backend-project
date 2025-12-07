package salesSavvy.service;

import java.util.List;

import salesSavvy.entity.Product;

public interface ProductService {

    Product addProduct(Product product);

    Product updateProduct(Product product);

    boolean deleteProduct(Long id);

    List<Product> getAllProducts();

    List<Product> searchProducts(String query);
}