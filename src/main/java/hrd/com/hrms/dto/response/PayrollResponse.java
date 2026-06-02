package hrd.com.hrms.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PayrollResponse {
    private UUID id;
    private UUID employeeId;
    private String employeeFullName;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private BigDecimal basicSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netPay;
    private LocalDate paymentDate;
}