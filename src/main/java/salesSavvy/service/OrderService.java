package salesSavvy.service;


import java.util.List;

import salesSavvy.dto.CheckoutRequest;
import salesSavvy.dto.OrderHistoryDTO;
import salesSavvy.entity.UserOrder;

public interface OrderService {
    UserOrder checkout(CheckoutRequest req);

	List<UserOrder> getOrdersByUser(String username);

	List<UserOrder> getAllOrders();

	void updateStatus(Long orderId, String status);

	List<OrderHistoryDTO> getOrderHistory(String username);
}
