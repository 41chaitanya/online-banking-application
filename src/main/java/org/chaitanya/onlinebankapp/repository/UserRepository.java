package org.chaitanya.onlinebankapp.repository;

import org.chaitanya.onlinebankapp.dto.RegisterRequest;
import org.chaitanya.onlinebankapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

  User findByEmail(String email);

}
