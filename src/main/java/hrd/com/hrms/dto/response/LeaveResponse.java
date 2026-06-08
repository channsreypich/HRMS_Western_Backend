package hrd.com.hrms.dto.response;

import hrd.com.hrms.enums.LeaveStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class LeaveResponse {
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private String employeeCode;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private long durationDays;
    private String reason;
    private LeaveStatus status;
    private String documentPath;
}