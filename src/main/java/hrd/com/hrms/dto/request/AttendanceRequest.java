package hrd.com.hrms.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class AttendanceRequest {
    private UUID employeeId;
    private String scanType; // e.g., "QR"
}