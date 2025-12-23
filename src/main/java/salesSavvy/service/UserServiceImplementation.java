package salesSavvy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import salesSavvy.dto.LoginData;
import salesSavvy.entity.User;
import salesSavvy.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // SIGN UP (REGISTER USER)
    @Override
    public String addUser(User user) {

        if (repo.findByUsername(user.getUsername()).isPresent()) {
            return "fail"; // username already exists
        }

        // BCrypt encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        repo.save(user);
        return "success";
    }

    // FETCH USER
    @Override
    public Optional<User> getUser(String username) {
        return repo.findByUsername(username);
    }

	@Override
	public String validateUser(LoginData data) {
		// TODO Auto-generated method stub
		return null;
	}
}
