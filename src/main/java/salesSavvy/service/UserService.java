package salesSavvy.service;

import java.util.Optional;
import salesSavvy.dto.LoginData;
import salesSavvy.entity.User;

public interface UserService {
    String addUser(User user);
    Optional<User> getUser(String username);
    String validateUser(LoginData data);
    
}