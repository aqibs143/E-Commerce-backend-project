package salesSavvy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import salesSavvy.entity.Product;
import salesSavvy.service.ProductService;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")  // adjust if needed
public class ProductController {

    @Autowired
    private ProductService service;
  
     //ADD PRODUCT
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody Product prod) {
        Product saved = service.addProduct(prod);
        return ResponseEntity.ok(saved);  // return saved product, not string
    }
  
     // UPDATE PRODUCT
    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody Product prod) {
        Product updated = service.updateProduct(prod);
        return ResponseEntity.ok(updated);
    }
 
     //DELETE PRODUCT
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id) {
        boolean deleted = service.deleteProduct(id);

        if (!deleted) {
            return ResponseEntity.status(404).body("Product not found");
        }

        return ResponseEntity.ok("Product deleted successfully");
    }

     // GET ALL PRODUCTS
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> list = service.getAllProducts();
        return ResponseEntity.ok(list);   // ALWAYS return clean JSON array
    }

     // SEARCH PRODUCTS
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String query) {
        List<Product> results = service.searchProducts(query);
        return ResponseEntity.ok(results);
    }
}
