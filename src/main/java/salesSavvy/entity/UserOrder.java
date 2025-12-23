package salesSavvy.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore   // Prevent recursion: user → orders → user
    private User user;

    private Double totalAmount;
    private String status;      
    private String paymentMode;  
    private String address;

    @CreationTimestamp
    private Timestamp orderDate;

    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference      // pair with @JsonBackReference in OrderItem
    private List<OrderItem> items = new ArrayList<>();

    public UserOrder() {}

    public UserOrder(User user, Double totalAmount, String paymentMode, String address, String status) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
        this.address = address;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }
}
