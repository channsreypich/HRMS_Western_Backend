package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class DepartmentResponse {
    private UUID id;
    private String name;
    private String code;
    private String color;
    private LocalDateTime createdAt;
}