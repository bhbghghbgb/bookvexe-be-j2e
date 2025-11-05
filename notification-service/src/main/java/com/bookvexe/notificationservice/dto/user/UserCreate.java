package com.bookvexe.notificationservice.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserCreate {
    private String username;
    private String password;
    private Boolean isGoogle;
    private String googleAccount;
    private UUID employeeId;
    private UUID customerId;
}
