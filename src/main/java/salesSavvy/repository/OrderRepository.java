package salesSavvy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import salesSavvy.entity.UserOrder;
import salesSavvy.entity.User;

public interface OrderRepository extends JpaRepository<UserOrder, Long> {

	List<UserOrder> findByUser(User user);}