package hrd.com.hrms.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PayrollRequest {
    @NotNull(message = "Employee matching target identifier required")
    private UUID employeeId;
    @NotNull(message = "Period context missing")
    private LocalDate payPeriodStart;
    @NotNull(message = "Period timeline edge missing")
    private LocalDate payPeriodEnd;
    @NotNull(message = "Base wage context calculation scale constraint required")
    private BigDecimal basicSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
}