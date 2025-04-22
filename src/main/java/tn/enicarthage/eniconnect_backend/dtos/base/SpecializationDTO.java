package tn.enicarthage.eniconnect_backend.dtos.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecializationDTO {
    private Long id;
    private String name;
    private String code;
}
