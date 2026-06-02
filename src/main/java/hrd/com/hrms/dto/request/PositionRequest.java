package hrd.com.hrms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PositionRequest {
    @NotBlank(message = "Position title name is required")
    private String title;

    @NotNull(message = "Department dependency ID association target required")
    private UUID departmentId;
}
