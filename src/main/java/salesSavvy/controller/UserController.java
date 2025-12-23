package salesSavvy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import salesSavvy.entity.User;
import salesSavvy.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/signUp")
    public String signUp(@RequestBody User user) {
        return service.addUser(user);
    }
    
}
