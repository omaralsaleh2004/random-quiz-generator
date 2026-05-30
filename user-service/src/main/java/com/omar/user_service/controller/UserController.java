package com.omar.user_service.controller;


import com.omar.user_service.dto.*;
import com.omar.user_service.exception.IncorrectPassword;
import com.omar.user_service.exception.NotFoundException;
import com.omar.user_service.model.User;
import com.omar.user_service.repo.UserRepo;
import com.omar.user_service.service.JwtService;
import com.omar.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest registerRequest) {

        User exisistUser = userRepo.findByEmail(registerRequest.getEmail());

        if(exisistUser != null) {
            throw new NotFoundException("User is Already Exists");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(registerRequest.getRole().toLowerCase().equals("admin") ? UserRole.ADMIN :UserRole.STUDENT);
        user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));

        User savedUser = userRepo.save(user);
        String token = jwtService.generateToken(savedUser.getId(),savedUser.getEmail() , savedUser.getRole().name());


        return ResponseEntity.ok(new TokenResponse(token));

    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail());

        if(user == null) {
            throw new NotFoundException("User Not Found");
        }

        if(!new BCryptPasswordEncoder().matches(loginRequest.getPassword(),user.getPassword())) {
            throw  new IncorrectPassword("Incorrect Password Please try again");
        }

        String token = jwtService.generateToken(user.getId(),user.getEmail() , user.getRole().name());

        return ResponseEntity.ok(new TokenResponse(token));

    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/all")
    public Map<Integer , UserDto> getUsersByIds(@RequestBody List<Integer> userIds) {
        return userService.getUsersByIds(userIds);
    }

}
