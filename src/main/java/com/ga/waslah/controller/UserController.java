package com.ga.waslah.controller;


import com.ga.waslah.model.User;
import com.ga.waslah.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {

        User newUser = userService.register(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
    @GetMapping("/test")
    public String test() {
        return "JWT works!";
    }
}
