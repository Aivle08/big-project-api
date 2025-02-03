package com.aivle08.big_project_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    @Column
    @JsonIgnore
    private String password;
    @Column
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String position;
    @Column
    private String contact;
    @Column(nullable = false)
    private boolean verifiedEmail;
    @Column
    private String verificationToken;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Department department;

}
