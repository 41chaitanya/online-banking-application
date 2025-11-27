package org.chaitanya.onlinebankapp.configAndSecurity;

import org.chaitanya.onlinebankapp.model.User;
import org.chaitanya.onlinebankapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
           User userFoundedByEmail = userRepository.findByEmail(email);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(userFoundedByEmail.getEmail())// as the method have to be use name but we are fetching by the email
                    .password(userFoundedByEmail.getPassword())
                    .roles(userFoundedByEmail.getRole().name())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e+" user not found ");
        }

    }
}
