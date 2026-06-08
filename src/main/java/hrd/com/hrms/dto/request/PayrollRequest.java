package hrd.com.hrms.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class PayrollRequest {
    @NotNull(message = "Employee ID cannot be empty")
    private UUID employeeId;

    @Min(value = 0, message = "Basic salary must be greater than or equal to 0")
    private double basicSalary;

    @Min(value = 0, message = "Allowances must be greater than or equal to 0")
    private double allowances;

    @Min(value = 0, message = "Deductions must be greater than or equal to 0")
    private double deductions;
}