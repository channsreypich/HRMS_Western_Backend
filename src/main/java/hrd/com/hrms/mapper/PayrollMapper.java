package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.model.Payroll;
import org.springframework.stereotype.Component;

@Component
public class PayrollMapper {
    public PayrollResponse toResponse(Payroll payroll) {
        if (payroll == null) return null;

        var emp = payroll.getEmployee();
        String first = emp != null && emp.getFirstName() != null ? emp.getFirstName() : "";
        String last = emp != null && emp.getLastName() != null ? emp.getLastName() : "";
        String fullName = (first + " " + last).trim();
        if (fullName.isEmpty()) fullName = "Unknown Employee";

        return PayrollResponse.builder()
                .id(payroll.getId())
                .employeeId(emp != null ? emp.getId() : null)
                .employeeName(fullName)
                .firstName(emp != null ? emp.getFirstName() : null)
                .lastName(emp != null ? emp.getLastName() : null)
                .employeeCode(emp != null ? emp.getEmployeeCode() : null)
                .departmentName(emp != null && emp.getDepartment() != null ? emp.getDepartment().getName() : null)
                .positionTitle(emp != null && emp.getPosition() != null ? emp.getPosition().getTitle() : null)
                .paymentDate(payroll.getPaymentDate())
                .basicSalary(payroll.getBasicSalary())
                .allowances(payroll.getAllowances())
                .deductions(payroll.getDeductions())
                .netPay(payroll.getNetPay())
                .status(payroll.getStatus() != null ? payroll.getStatus() : "draft")
                .build();
    }
}