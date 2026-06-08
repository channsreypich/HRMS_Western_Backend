package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.PayrollRequest;
import hrd.com.hrms.dto.response.PayrollResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface PayrollService {
    PayrollResponse calculateAndSavePayroll(PayrollRequest request);
    PayrollResponse getPayrollById(UUID id);
    Page<PayrollResponse> getEmployeePayrollHistory(UUID employeeId, Pageable pageable);
    Page<PayrollResponse> getAllPayrollRecords(Pageable pageable);
    void deletePayroll(UUID id);
}