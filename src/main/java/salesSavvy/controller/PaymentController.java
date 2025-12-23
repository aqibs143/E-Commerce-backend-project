package salesSavvy.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import salesSavvy.entity.*;
import salesSavvy.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired 
    private UserRepository userRepo;
    @Autowired 
    private CartRepository cartRepo;
    @Autowired 
    private OrderRepository orderRepo;
    @Autowired 
    private OrderItemRepository orderItemRepo;
    @Autowired 
    private CartItemRepository itemRepo;

    // TEST KEYS — move to config / env variables in production
    private static final String RAZOR_KEY = "rzp_test_A0pogOFt1fVWQe";
    private static final String RAZOR_SECRET = "KMI7TRVgCayp1BvrtWvIQ6X1";

  
    //  CREATE RAZORPAY ORDER
    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestParam String username) throws Exception {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart Not Found"));

        // SUM in rupees using doubles safely, then convert to paise (long)
        double totalRupees = cart.getItemList()
                .stream()
                .mapToDouble(ci -> {
                    Product p = ci.getProd();
                    if (p == null) return 0.0;
                    return ci.getQuantity() * (p.getPrice() == null ? 0.0 : p.getPrice());
                })
                .sum();

        // Convert to paise precisely (long)
        long amountInPaise = BigDecimal.valueOf(totalRupees)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();

        RazorpayClient client = new RazorpayClient(RAZOR_KEY, RAZOR_SECRET);

        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "receipt_" + user.getId());

        Order razorpayOrder = client.orders.create(options);

        return ResponseEntity.ok(Map.of(
                "orderId", razorpayOrder.get("id"),
                "amount", amountInPaise,
                "currency", "INR"
        ));
    }

  
    // CONFIRM PAYMENT
    @PostMapping("/confirm")
    @Transactional
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, Object> data) {

        String username = String.valueOf(data.get("username"));
        String paymentId = String.valueOf(data.get("razorpay_payment_id"));
        String orderId = String.valueOf(data.get("razorpay_order_id"));
        String address = data.getOrDefault("address", "NO ADDRESS").toString();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // compute total as BigDecimal rupees
        BigDecimal totalRupees = cart.getItemList()
                .stream()
                .map(ci -> {
                    Product p = ci.getProd();
                    double price = (p == null || p.getPrice() == null) ? 0.0 : p.getPrice();
                    return BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(ci.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // persist order (store rupee amount)
        UserOrder order = new UserOrder();
        order.setUser(user);
        order.setTotalAmount(totalRupees.doubleValue());
        order.setPaymentMode("ONLINE");
        order.setStatus("PAID");
        order.setAddress(address);
        orderRepo.save(order); // now order has an ID

        // create order items — skip entries with null product
        for (CartItem ci : cart.getItemList()) {
            Product prod = ci.getProd();
            if (prod == null) {
                // product was removed — skip or record placeholder if you want
                continue;
            }

            OrderItem oi = new OrderItem();
            // use the correct setter name based on OrderItem entity:
            // if entity uses setUserOrder(...) use that if setOrder(...) exists make sure it's implemented.
            oi.setUserOrder(order);                      // correct method in the provided entity
            oi.setProduct(prod);                         // product setter exists
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtPurchase(prod.getPrice());
            orderItemRepo.save(oi);
        }

        // clear cart
        itemRepo.deleteAll(cart.getItemList());
        cart.getItemList().clear();
        cartRepo.save(cart); // persist changes to cart if needed

        return ResponseEntity.ok(Map.of(
                "message", "Payment Successful",
                "paymentId", paymentId,
                "orderId", orderId,
                "total", totalRupees.doubleValue()
        ));
    }
}
