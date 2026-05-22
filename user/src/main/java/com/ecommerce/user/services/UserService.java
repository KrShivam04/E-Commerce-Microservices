package com.ecommerce.user.services;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService
{

    public List<UserResponse> fetchAllUsers();
    public void addUser(UserRequest userRequest);
    public Optional<UserResponse> getUserById(String id);
    public Boolean updateUser(String id, UserRequest userRequest);

}
