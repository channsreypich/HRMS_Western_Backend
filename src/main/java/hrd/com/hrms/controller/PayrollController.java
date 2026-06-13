package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.MonthlyPayrollRequest;
import hrd.com.hrms.dto.request.PayrollRequest;
import hrd.com.hrms.dto.request.PayrollStatusRequest;
import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.service.PayrollService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PayrollResponse>> generatePayroll(@Valid @RequestBody PayrollRequest request) {
        PayrollResponse response = payrollService.calculateAndSavePayroll(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Payroll ledger record built successfully", response));
    }

    @PostMapping("/generate-monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateMonthlyPayroll(@Valid @RequestBody MonthlyPayrollRequest request) {
        int created = payrollService.generateMonthlyPayroll(request.paymentMonth());
        Map<String, Object> result = Map.of("payment_month", request.paymentMonth(), "generated", created);
        String message = created > 0
                ? created + " payroll record(s) generated for " + request.paymentMonth()
                : "No new payroll records to generate for " + request.paymentMonth() + " (already up to date)";
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), message, result));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<PayrollResponse>> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody PayrollStatusRequest request) {
        PayrollResponse response = payrollService.updatePayrollStatus(id, request.status());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Payroll status updated successfully", response));
    }

    @GetMapping("/history/{employeeId}")
    public ResponseEntity<ApiResponse<Page<PayrollResponse>>> getHistory(@PathVariable UUID employeeId, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Pay stubs synchronized successfully", payrollService.getEmployeePayrollHistory(employeeId, pageable)));
    }

    @GetMapping("/records")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Page<PayrollResponse>>> getAllRecords(Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Financial master log parsed successfully", payrollService.getAllPayrollRecords(pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<PayrollResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Payroll record found", payrollService.getPayrollById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        payrollService.deletePayroll(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Payroll record deleted successfully", null));
    }
}