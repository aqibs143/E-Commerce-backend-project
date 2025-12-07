package salesSavvy.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import salesSavvy.repository.CartItemRepository;

@Service
public class CartItemServiceImplementation 
					implements CartItemService {
	@Autowired
	CartItemRepository repo;

}