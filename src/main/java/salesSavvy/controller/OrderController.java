package salesSavvy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import salesSavvy.dto.CheckoutRequest;
import salesSavvy.dto.OrderHistoryDTO;
import salesSavvy.entity.UserOrder;
import salesSavvy.service.OrderService;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @PostMapping("/checkout")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<UserOrder> checkout(@RequestBody CheckoutRequest request) {
        UserOrder userOrder = orderService.checkout(request);
        return ResponseEntity.ok(userOrder);
    }
//    @GetMapping("/history")
//    public ResponseEntity<List<UserOrder>> getOrderHistory(@RequestParam String username) {
//        List<UserOrder> userOrders = orderService.getOrdersByUser(username);
//        return ResponseEntity.ok(userOrders);
//    }
    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryDTO>> getOrderHistory(@RequestParam String username) {

        List<OrderHistoryDTO> history = orderService.getOrderHistory(username);

        return ResponseEntity.ok(history);
    }

    @GetMapping("/all")
    public List<UserOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateOrderStatus(@RequestParam Long orderId, @RequestParam String status) {
        orderService.updateStatus(orderId, status);
        return ResponseEntity.ok("UserOrder status updated successfully");
    }


}
