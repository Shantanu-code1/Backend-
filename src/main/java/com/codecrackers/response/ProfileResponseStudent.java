package com.codecrackers.response;

import com.codecrackers.model.AvailableForDoubts;
import com.codecrackers.model.USER_ROLE;
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
    private boolean verify;
    private String rating;
    private String slots;
    private String done;
    private String pending;
    private String feedback;
    private String earning;
    private String totalEarning;
    private USER_ROLE role;
    private AvailableForDoubts availableForDoubts;
}
