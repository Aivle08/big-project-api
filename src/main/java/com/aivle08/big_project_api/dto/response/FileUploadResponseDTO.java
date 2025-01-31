package com.aivle08.big_project_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponseDTO {
    private String status;
    private String message;
    private List<String> fileNames;
}
