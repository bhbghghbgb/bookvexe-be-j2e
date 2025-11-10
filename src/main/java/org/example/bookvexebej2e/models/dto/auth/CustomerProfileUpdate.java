package org.example.bookvexebej2e.models.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerProfileUpdate {
    private String name;

    @Email
    private String email;

    private String phone;

    private String password; // Optional for setting/updating password
}