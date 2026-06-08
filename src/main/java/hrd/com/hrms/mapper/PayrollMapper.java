package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.model.Payroll;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {
    public PayrollResponse toResponse(Payroll payroll) {
        if (payroll == null) return null;

        String fullName = "Unknown Employee";
        if (payroll.getEmployee() != null) {
            fullName = payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName();
        }

        return PayrollResponse.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployee().getId())
                .employeeName(fullName)
                .paymentDate(payroll.getPaymentDate())
                .basicSalary(payroll.getBasicSalary())
                .allowances(payroll.getAllowances())
                .deductions(payroll.getDeductions())
                .netPay(payroll.getNetPay())
                .build();
    }
}