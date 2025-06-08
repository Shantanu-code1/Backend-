package com.codecrackers.response;

import com.codecrackers.model.USER_ROLE;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String jwt;
    private boolean status;
    private String message;
    private USER_ROLE role;
    private Long studentId;
}
