package hrd.com.hrms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class LeaveRequest {
    @NotNull(message = "Employee identification is required")
    private UUID employeeId;

    @NotBlank(message = "Leave domain type classification field missing")
    private String leaveType;

    @NotNull(message = "Start date missing")
    @FutureOrPresent(message = "Start date cannot fall in past timelines")
    private LocalDate startDate;

    @NotNull(message = "End date missing")
    private LocalDate endDate;

    @NotBlank(message = "Justification text missing")
    private String reason;
}