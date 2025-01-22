package com.aivle08.big_project_api.dto.output;

import com.aivle08.big_project_api.dto.RecruitmentDTO;
import com.aivle08.big_project_api.model.Department;
import com.aivle08.big_project_api.model.Recruitment;
import com.aivle08.big_project_api.model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsersOutputDTO {
    private String name;
    private String companyName;
    private String email;
    private String contact;
    private String departmentName;
    private String position;
    private List<RecruitmentDTO> recruitmentDTOList;

    public static UsersOutputDTO fromEntity(Users users) {
        UsersOutputDTO usersOutputDTO = new UsersOutputDTO();
        usersOutputDTO.setName(users.getName());
        usersOutputDTO.setCompanyName(users.getCompany().getName());
        usersOutputDTO.setEmail(users.getEmail());
        usersOutputDTO.setContact(users.getContact());
        usersOutputDTO.setDepartmentName(users.getDepartment().getName());
        usersOutputDTO.setPosition(users.getPosition());
        List<RecruitmentDTO> recruitmentDTOS = new ArrayList<>();
        for(Department d : users.getCompany().getDepartmentList()){
            RecruitmentDTO recruitmentDTO = new RecruitmentDTO();
            recruitmentDTO.setJob(d.getName());
            recruitmentDTO.setTitle(d.getName());
            recruitmentDTOS.add(recruitmentDTO);
        }
        usersOutputDTO.setRecruitmentDTOList(recruitmentDTOS);
        return usersOutputDTO;
    }
}
