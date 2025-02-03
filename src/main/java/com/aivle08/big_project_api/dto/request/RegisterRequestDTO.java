package com.aivle08.big_project_api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
    private String userId;
    private String password;
    private String username;
    private String contact;
    private String email;
    private String position;
    private String companyName;
    private String departmentName;
}
