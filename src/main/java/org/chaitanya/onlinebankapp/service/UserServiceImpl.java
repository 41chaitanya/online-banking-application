package org.chaitanya.onlinebankapp.service;

import org.chaitanya.onlinebankapp.dto.RegisterRequest;
import org.chaitanya.onlinebankapp.model.User;
import org.chaitanya.onlinebankapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passWordEncoder;

    @Override
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail())!=null){
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passWordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

}
