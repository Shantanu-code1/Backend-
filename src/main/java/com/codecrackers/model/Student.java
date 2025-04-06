package com.codecrackers.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "STUDENTS_USERS")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Doubt> doubts;

    @OneToMany
    private List<Review> review;

    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String otp;
    private boolean verify;
    private Long shares = 0L;
    private Long points = 0L;
    private boolean doubtFree = false;
    private String profileImage;
    private String rating;
    private String slots;
    private String done;
    private String pending;
    private String feedback;
    private String earning;
    private String totalEarning;
    private String codingProfileLink;
    private USER_ROLE role = USER_ROLE.ROLE_STUDENT;
    private AvailableForDoubts availableForDoubts = AvailableForDoubts.ONLINE;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnyQuery> query;
}
