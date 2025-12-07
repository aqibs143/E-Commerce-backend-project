package salesSavvy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import salesSavvy.dto.LoginData;
import salesSavvy.entity.User;
import salesSavvy.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    private UserRepository repo;

    @Override
    public String addUser(User user) {
        // Check if username already exists
        Optional<User> existing = repo.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            return "fail";   // already taken
        }
        // New username — save
        repo.save(user);
        return "success";
    }

    @Override
    public Optional<User> getUser(String username) {
        // Just wrap repo call
        return repo.findByUsername(username);
    }
    @Override
    public String validateUser(LoginData data) {

        Optional<User> optUser = repo.findByUsername(data.getUsername());
        if (!optUser.isPresent()) {
            return "invalid"; // user not found
        }

        User u = optUser.get();

        // Null-safe password check
        if (u.getPassword() == null || data.getPassword() == null ||
            !u.getPassword().equals(data.getPassword())) {

            System.out.println("Password mismatch or null password!");
            return "invalid";
        }

        // Null-safe role check
        String role = u.getRole();
        if (role == null) return "customer";

        return role.equalsIgnoreCase("ADMIN") ? "admin" : "customer";
    }

  }