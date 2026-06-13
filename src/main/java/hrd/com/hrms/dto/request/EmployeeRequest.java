package hrd.com.hrms.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class EmployeeRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("first_name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    private String email;

    @JsonProperty("role_id")
    private UUID roleId; // optional; defaults to ROLE_EMPLOYEE when omitted

    @JsonProperty("department_id")
    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @JsonProperty("position_id")
    @NotNull(message = "Position ID is required")
    private UUID positionId;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("status")
    private String status;

    @JsonProperty("hire_date")
    private LocalDate hireDate;

    @JsonProperty("base_salary")
    private BigDecimal baseSalary;
}