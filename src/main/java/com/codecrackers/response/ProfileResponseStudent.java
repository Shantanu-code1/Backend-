package com.codecrackers.response;

import lombok.Data;

@Data
public class ProfileResponseStudent {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Long shares;
    private Long points;
    private boolean doubtFree;
    private String profileImage;
    private String codingProfileLink;
}
