package org.chaitanya.onlinebankapp.controller;

import org.chaitanya.onlinebankapp.config.JwtUtil;
import org.chaitanya.onlinebankapp.dto.LoginRequest;
import org.chaitanya.onlinebankapp.dto.LoginResponse;
import org.chaitanya.onlinebankapp.dto.RegisterRequest;
import org.chaitanya.onlinebankapp.model.User;
import org.chaitanya.onlinebankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;



//    registration
    @PostMapping("/register")
    public String raigster(@RequestBody RegisterRequest registerRequest){
        if(userRepository.findByEmail(registerRequest.getEmail())!=null){
            return "user already exists";
        }
        User userThatBuild = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        userRepository.save(userThatBuild);
        return "user registered successfully";
    }

//    login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), loginRequest.getPassword()
                )
        );
        String token = jwtUtil.generateToken(authenticate);

        User userThatExist = userRepository.findByEmail(loginRequest.getEmail());
        return new LoginResponse(
                token,
                userThatExist.getEmail(),
                userThatExist.getRole().name()
        );

    }






}
