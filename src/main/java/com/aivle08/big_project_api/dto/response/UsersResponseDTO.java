package com.aivle08.big_project_api.dto.response;

import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDTO {
    private String name;
    private String companyName;
    private String email;
    private String contact;
    private String departmentName;
    private String position;
    private List<RecruitmentItemResponseDTO> recruitmentItemResponseDTOList;

    public static UsersResponseDTO fromEntity(Users users) {
        UsersResponseDTO usersResponseDTO = new UsersResponseDTO();
        usersResponseDTO.setName(users.getName());
        usersResponseDTO.setCompanyName(users.getCompany().getName());
        usersResponseDTO.setEmail(users.getEmail());
        usersResponseDTO.setContact(users.getContact());
        usersResponseDTO.setDepartmentName(users.getDepartment().getName());
        usersResponseDTO.setPosition(users.getPosition());
        List<RecruitmentItemResponseDTO> recruitmentItemResponseDTOS = new ArrayList<>();
        for (Department d : users.getCompany().getDepartmentList()) {
            RecruitmentItemResponseDTO recruitmentItemResponseDTO = new RecruitmentItemResponseDTO();
            recruitmentItemResponseDTO.setJob(d.getName());
            recruitmentItemResponseDTO.setTitle(d.getName());
            recruitmentItemResponseDTOS.add(recruitmentItemResponseDTO);
        }
        usersResponseDTO.setRecruitmentItemResponseDTOList(recruitmentItemResponseDTOS);
        return usersResponseDTO;
    }
}
