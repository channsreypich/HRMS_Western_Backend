package hrd.com.hrms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    // Required on create, optional on update (leave blank to keep the current password)
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Role ID is required")
    private UUID roleId;

    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @NotNull(message = "Position ID is required")
    private UUID positionId;
    private String phone;
    private String status;
    private LocalDate hireDate;
    private BigDecimal baseSalary;
}
