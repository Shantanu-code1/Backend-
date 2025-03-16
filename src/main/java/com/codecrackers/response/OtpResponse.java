package com.codecrackers.response;

import com.codecrackers.model.Student;
import lombok.Data;

@Data
public class OtpResponse {
    private String status;
    private String message;
    private Student student;
}
