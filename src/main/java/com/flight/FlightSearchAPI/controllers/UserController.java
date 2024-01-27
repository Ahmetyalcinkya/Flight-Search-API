package com.flight.FlightSearchAPI.controllers;

import com.flight.FlightSearchAPI.dto.responses.UserResponse;
import com.flight.FlightSearchAPI.services.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }
    @DeleteMapping("/admin/deleteUser/{id}")
    public UserResponse deleteUser(@PathVariable long id){
        return userService.deleteUser(id);
    }
}
