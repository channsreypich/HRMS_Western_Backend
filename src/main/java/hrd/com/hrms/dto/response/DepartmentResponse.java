package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class DepartmentResponse {
    private UUID id;
    private String code;
    private String name;
}