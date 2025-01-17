package com.aivle08.big_project_api.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInputDTO {
    private String userId;
    private String password;
    private String username;
    private String contact;
    private String email;
    private String position;
    private String companyName;
    private String address;
    private String departmentName;
}
