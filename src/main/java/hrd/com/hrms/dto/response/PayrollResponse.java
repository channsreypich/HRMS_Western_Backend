package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PayrollResponse {
    private UUID id;
    private UUID employeeId;
    private String employeeName;
    private LocalDate paymentDate;
    private double basicSalary;
    private double allowances;
    private double deductions;
    private double netPay;
}