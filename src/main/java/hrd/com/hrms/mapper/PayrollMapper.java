package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.model.Payroll;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {
    public PayrollResponse toResponse(Payroll payroll) {
        if (payroll == null) return null;

        String fullName = (payroll.getEmployee() != null)
                ? payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName()
                : "Unknown Employee";

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