package org.chaitanya.onlinebankapp.dto;

import lombok.Data;
import org.chaitanya.onlinebankapp.model.UserRoles;

@Data
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private UserRoles role;
}
