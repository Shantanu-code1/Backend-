package com.codecrackers.request;

import com.codecrackers.model.USER_ROLE;
import lombok.Data;

@Data
public class SingUpRequest {
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String otp;
    private USER_ROLE role;
}
