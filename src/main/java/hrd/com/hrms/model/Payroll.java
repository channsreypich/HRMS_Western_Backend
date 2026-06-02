package hrd.com.hrms.model;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class Payroll {

    private UUID id;

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Pay period start date is required")
    private LocalDate payPeriodStart;

    @NotNull(message = "Pay period end date is required")
    private LocalDate payPeriodEnd;

    @NotNull(message = "Basic salary is required")
    @DecimalMin(value = "0.00", message = "Basic salary cannot be negative")
    private BigDecimal basicSalary;

    @DecimalMin(value = "0.00", message = "Allowances cannot be negative")
    private BigDecimal allowances = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "Deductions cannot be negative")
    private BigDecimal deductions = BigDecimal.ZERO;

    @NotNull(message = "Net pay is required")
    @DecimalMin(value = "0.00", message = "Net pay cannot be negative")
    private BigDecimal netPay;

    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;
}