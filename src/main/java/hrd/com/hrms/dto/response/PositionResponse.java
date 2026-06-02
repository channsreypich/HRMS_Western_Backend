package hrd.com.hrms.dto.response;

import lombok.Data;
import java.util.UUID;

@Data
public class PositionResponse {
    private UUID id;
    private String title;
    private UUID departmentId;
}