package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.Users;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersResponseDTO {
    private String name;
    private String companyName;
    private String email;
    private String contact;
    private String departmentName;
    private String position;
    private List<RecruitmentItemResponseDTO> recruitmentItemResponseDTOList;

    public static UsersResponseDTO fromEntity(Users users) {
        List<RecruitmentItemResponseDTO> recruitmentItemResponseDTOS =
                users.getCompany().getDepartmentList()
                        .stream()
                        .map(d -> RecruitmentItemResponseDTO.builder()
                                .job(d.getName())
                                .title(d.getName())
                                .build())
                        .collect(Collectors.toList());

        return UsersResponseDTO.builder()
                .name(users.getName())
                .companyName(users.getCompany().getName())
                .email(users.getEmail())
                .contact(users.getContact())
                .departmentName(users.getDepartment().getName())
                .position(users.getPosition())
                .recruitmentItemResponseDTOList(recruitmentItemResponseDTOS)
                .build();
    }
}
