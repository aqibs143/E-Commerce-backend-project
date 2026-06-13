package salesSavvy.auth;

import salesSavvy.dto.LoginData;
import salesSavvy.entity.User;
import salesSavvy.repository.UserRepository;
import salesSavvy.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController 	{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginData loginData) {

        // Authenticate credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.getUsername(),
                            loginData.getPassword()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Bad credentials: " + e.getMessage());
        }


        // Fetch user from DB
        User user = userRepository
                .findByUsername(loginData.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT with ROLE
        String token = jwtUtil.generateToken(
                user.getUsername(),
                "ROLE_" + user.getRole()
        );

        // Return token
        return ResponseEntity.ok(token);
    }
}
