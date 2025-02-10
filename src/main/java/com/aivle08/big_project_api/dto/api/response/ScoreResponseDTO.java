package com.aivle08.big_project_api.dto.api.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResponseDTO {
    private Integer score;
    private String reason;
    private List<String> chunk;
}