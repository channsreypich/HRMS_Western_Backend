package hrd.com.hrms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttendanceRequest {
    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Timestamp cannot be empty")
    private LocalDateTime timestamp;

    private String qrCodeToken; // Decrypted data validated from public QR scans
}