package hrd.com.hrms.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeaveResponse {
    private UUID id;
    private UUID employeeId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status;
}