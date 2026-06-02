package hrd.com.hrms.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttendanceResponse {
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String status; // Present, Late, Absent

    public void setClockInTime(Object clockInTime) {
    }

    public void setClockOutTime(Object clockOutTime) {
    }
}