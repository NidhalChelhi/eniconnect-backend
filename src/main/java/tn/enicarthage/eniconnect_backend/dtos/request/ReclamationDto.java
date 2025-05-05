package tn.enicarthage.eniconnect_backend.dtos.request;



import lombok.Data;
import tn.enicarthage.eniconnect_backend.enums.ReclamationType;

@Data
public class ReclamationDto {
    private Long studentId; // Nullable: if null → anonymous
    private ReclamationType type;
    private String message;
}
