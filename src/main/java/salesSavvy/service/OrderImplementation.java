package salesSavvy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salesSavvy.dto.CheckoutRequest;
import salesSavvy.dto.OrderHistoryDTO;
import salesSavvy.dto.OrderItemDTO;
import salesSavvy.entity.Cart;
import salesSavvy.entity.CartItem;
import salesSavvy.entity.UserOrder;
import salesSavvy.entity.OrderItem;
import salesSavvy.entity.Product;
import salesSavvy.entity.User;
import salesSavvy.repository.CartItemRepository;
import salesSavvy.repository.CartRepository;
import salesSavvy.repository.OrderItemRepository;
import salesSavvy.repository.OrderRepository;
import salesSavvy.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderImplementation implements OrderService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CartItemRepository itemRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Override
    @Transactional
    public UserOrder checkout(CheckoutRequest req) {
        User user = userRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cart.getItemList();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProd().getPrice() * item.getQuantity())
                .sum();

        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setAddress(req.getAddress());
        userOrder.setPaymentMode(req.getPaymentMode());
        userOrder.setTotalAmount(totalAmount);
        userOrder.setStatus("PENDING");

        orderRepo.save(userOrder);

        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setUserOrder(userOrder);
            oi.setProduct(ci.getProd());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(ci.getProd().getPrice());
            orderItemRepo.save(oi);
        }

        // Clear cart after order
        itemRepo.deleteAll(cartItems);

        return userOrder;
    }
    public List<UserOrder> getOrdersByUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepo.findByUser(user);
    }
    
    public List<UserOrder> getAllOrders() {
        return orderRepo.findAll();
    }

    @Transactional
    public void updateStatus(Long orderId, String status) {
        UserOrder userOrder = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("UserOrder not found"));
        userOrder.setStatus(status);
    }
//	@Override
//	public List<OrderHistoryDTO> getOrderHistory(String username) {
//		// TODO Auto-generated method stub
//		return null;
//	}
    public List<OrderHistoryDTO> getOrderHistory(String username) {

    User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    List<UserOrder> orders = orderRepo.findByUser(user);

    List<OrderHistoryDTO> out = new ArrayList<>();

    for (UserOrder o : orders) {

        OrderHistoryDTO dto = new OrderHistoryDTO();
        dto.setOrderId(o.getOrderId());
        dto.setTotalAmount(o.getTotalAmount());
        dto.setStatus(o.getStatus());
        dto.setPaymentMode(o.getPaymentMode());
        dto.setAddress(o.getAddress());
        dto.setOrderDate(o.getOrderDate());

        List<OrderItem> orderItems = orderItemRepo.findByUserOrder(o);

        List<OrderItemDTO> itemDTOs = orderItems.stream()
                .map(it -> {

                    Product p = it.getProduct(); // could be null

                    Long productId = (p != null) ? p.getId() : null;
                    String name    = (p != null) ? p.getName() : "Product Removed";
                    double price   = it.getPriceAtPurchase();
                    int qty        = it.getQuantity();

                    return new OrderItemDTO(productId, name, qty, price);
                })
                .toList();

        dto.setItems(itemDTOs);

        out.add(dto);
    }

    return out;
}




}
