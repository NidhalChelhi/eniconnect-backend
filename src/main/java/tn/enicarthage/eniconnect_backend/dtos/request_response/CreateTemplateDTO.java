package tn.enicarthage.eniconnect_backend.dtos.request_response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import tn.enicarthage.eniconnect_backend.dtos.base.QuestionDTO;

import java.util.List;

@Data
@Builder
public class CreateTemplateDTO {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private List<QuestionDTO> questions;
}