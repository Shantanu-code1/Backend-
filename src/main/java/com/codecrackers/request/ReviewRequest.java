package com.codecrackers.request;

import com.codecrackers.model.Review;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Data
public class ReviewRequest {
    private String email;
    private Review review;
}
