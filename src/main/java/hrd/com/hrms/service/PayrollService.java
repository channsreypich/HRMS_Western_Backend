package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.PayrollRequest;
import hrd.com.hrms.dto.response.PayrollResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface PayrollService {
    PayrollResponse generatePayslip(PayrollRequest request);
    PayrollResponse getPayslipById(UUID id);
    List<PayrollResponse> getEmployeePayrollHistory(UUID employeeId);

    PayrollResponse processPayroll(@Valid PayrollRequest request);
}