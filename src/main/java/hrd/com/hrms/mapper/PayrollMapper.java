package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.payroll.PayrollRequest;
import hrd.com.hrms.dto.payroll.PayrollResponse;
import hrd.com.hrms.model.Payroll;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PayrollMapper {

    public Payroll toEntity(PayrollRequest request) {
        if (request == null) return null;

        Payroll payroll = new Payroll();
        payroll.setBasicSalary(request.getBasicSalary());
        payroll.setAllowances(request.getAllowances());
        payroll.setDeductions(request.getDeductions());
        payroll.setPayDate(request.getPayDate());
        return payroll;
    }

    public PayrollResponse toResponse(Payroll payroll) {
        if (payroll == null) return null;

        String fullName = "";
        if (payroll.getEmployee() != null) {
            fullName = payroll.getEmployee().getFirstName() + " " + payroll.getEmployee().getLastName();
        }

        // Calculate net salary runtime validation block logic safety execution
        BigDecimal basic = payroll.getBasicSalary() != null ? payroll.getBasicSalary() : BigDecimal.ZERO;
        BigDecimal allowances = payroll.getAllowances() != null ? payroll.getAllowances() : BigDecimal.ZERO;
        BigDecimal deductions = payroll.getDeductions() != null ? payroll.getDeductions() : BigDecimal.ZERO;
        BigDecimal netSalary = basic.add(allowances).subtract(deductions);

        return new PayrollResponse(
                payroll.getId(),
                payroll.getEmployee() != null ? payroll.getEmployee().getId() : null,
                fullName,
                basic,
                allowances,
                deductions,
                netSalary,
                payroll.getPayDate()
        );
    }
}