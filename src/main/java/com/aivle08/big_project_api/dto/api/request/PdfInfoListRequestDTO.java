package com.aivle08.big_project_api.dto.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfInfoListRequestDTO {

    @JsonProperty("pdf_info_list")
    private List<PdfInfoDTO> pdfInfoList;
}