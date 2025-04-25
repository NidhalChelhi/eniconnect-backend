package tn.enicarthage.eniconnect_backend.dtos.request_response;

import lombok.Builder;
import lombok.Data;
import tn.enicarthage.eniconnect_backend.dtos.base.TemplateQuestionDTO;

import java.util.List;

@Data
@Builder
public class TemplateResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<TemplateQuestionDTO> questions;
}