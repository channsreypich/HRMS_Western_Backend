package hrd.com.hrms.controller;

import hrd.com.hrms.dto.request.PayrollRequest;
import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.service.PayrollService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollRepositoryService;

    @PostMapping("/process")
    public ResponseEntity<PayrollResponse> generateSlip(@Valid @RequestBody PayrollRequest request) {
        return ResponseEntity.ok(payrollRepositoryService.processPayroll(request));
    }
}