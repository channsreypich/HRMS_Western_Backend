package hrd.com.hrms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class PositionRequest {
    @NotBlank(message = "Position title is required")
    private String title;

    private UUID departmentId;
}