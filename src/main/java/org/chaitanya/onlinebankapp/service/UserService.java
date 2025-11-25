package org.chaitanya.onlinebankapp.service;


import org.chaitanya.onlinebankapp.dto.LoginRequest;
import org.chaitanya.onlinebankapp.dto.LoginResponse;
import org.chaitanya.onlinebankapp.model.User;

public interface UserService {
    User register(User user);



}
