package com.aivle08.big_project_api.dto.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtractionResponseDTO {
    private String name;
    private String phone;
    private String email;
    private String birth;
    private String address;

    @JsonProperty("else_summary")
    private String elseSummary;
}
