package hrd.com.hrms.dto.request;

import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

// Lets HR enter an attendance record directly. JSON (snake_case):
// { "employee_id": "...", "date": "2026-06-09", "status": "PRESENT", "check_in": "08:30", "check_out": "17:00" }
@Data
public class ManualAttendanceRequest {
    private UUID employeeId;
    private String employeeCode;
    private LocalDate date;
    private String status;   // PRESENT | ABSENT | LATE | HALF_DAY
    private String checkIn;  // "HH:mm"
    private String checkOut; // "HH:mm"
}
