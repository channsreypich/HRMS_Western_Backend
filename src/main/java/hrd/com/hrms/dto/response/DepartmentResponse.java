package hrd.com.hrms.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class DepartmentResponse {
    private UUID id;
    private String name;
    private String description;
}