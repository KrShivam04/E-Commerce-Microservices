package com.ecommerce.user.controller;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController
{
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers()
    {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest)
    {
        userService.addUser(userRequest);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String id)
    {
        return userService.getUserById(id).map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest)
    {
        Boolean data = userService.updateUser(id, userRequest);
        if (data)
        {
            return new ResponseEntity<>("User updated", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


}
