package com.example.SBA_M.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}