package salesSavvy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private String image;

    //FIXED: Prevent recursion from Product → OrderItem → UserOrder
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    //FIXED: Prevent recursion from Product → CartItem → Cart
    @JsonIgnore
    @OneToMany(mappedBy = "prod")
    private List<CartItem> cartItems;

    public Product() {}

    public Long getId() {
    	return id; 
    	}
    
    public void setId(Long id) {
    	this.id = id; 
    	}

    public String getName() {
    	return name; 
    	}
    public void setName(String name) {
    	this.name = name; 
    	}

    public String getDescription() { 
    	return description; 
    	}
    
    public void setDescription(String description) {
    	this.description = description; 
    	}

    public Double getPrice() { 
    	return price; 
    	}
    
    public void setPrice(Double price) {
    	this.price = price; 
    	}

    public String getImage() { 
    	return image; 
    	}
    
    public void setImage(String image) { 
    	this.image = image; 
    }
}
