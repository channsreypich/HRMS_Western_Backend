package hrd.com.hrms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;
@Data
public class EmployeeRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private String phoneNumber;
    @NotNull(message = "Hire date is required")
    private LocalDate hireDate;
    @NotNull(message = "Department ID is required")
    private UUID departmentId;
    @NotNull(message = "Position ID is required")
    private UUID positionId;
}


