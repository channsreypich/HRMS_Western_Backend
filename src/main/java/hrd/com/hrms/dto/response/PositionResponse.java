package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class PositionResponse {
    private UUID id;
    private String title;
    private UUID departmentId;
    private String departmentName;
}