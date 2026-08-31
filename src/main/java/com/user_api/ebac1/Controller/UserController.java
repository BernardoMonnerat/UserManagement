package com.user_api.ebac1.Controller;

import com.user_api.ebac1.Exceptions.UserExceptions;
import com.user_api.ebac1.Model.User;
import com.user_api.ebac1.Request.UserRequest;
import com.user_api.ebac1.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest user){
        service.addUser(new User(user.getEmail(), user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body("User has been registered Successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequest user){
        boolean isValid = service.validateUser(new User(user.getEmail(), user.getPassword()));

        if(isValid){
           return ResponseEntity.status(HttpStatus.OK).body("Logged successfully!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed!");
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard(@RequestHeader("email")String email) {
        boolean isValid = service.temporarySession(email);

       if(isValid){
           return ResponseEntity.status(HttpStatus.OK).body("Welcome to Dashboard, you session is active!");
       }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access denied!");
    }

}
