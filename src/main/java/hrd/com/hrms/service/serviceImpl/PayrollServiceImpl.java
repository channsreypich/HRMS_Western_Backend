package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.PayrollRequest;
import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.model.Payroll;
import hrd.com.hrms.repository.PayrollRepository;
import hrd.com.hrms.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {
    private final PayrollRepository payrollRepository;

    @Override
    public PayrollResponse processPayroll(PayrollRequest request) {
        BigDecimal allowances = request.getAllowances() != null ? request.getAllowances() : BigDecimal.ZERO;
        BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : BigDecimal.ZERO;

        // Functional Net Calculation Implementation Logic: Net = Basic + Allowances - Deductions
        BigDecimal netPay = request.getBasicSalary().add(allowances).subtract(deductions);

        Payroll payroll = new Payroll();
        payroll.setId(UUID.randomUUID());
        payroll.setEmployeeId(request.getEmployeeId());
        payroll.setPayPeriodStart(request.getPayPeriodStart());
        payroll.setPayPeriodEnd(request.getPayPeriodEnd());
        payroll.setBasicSalary(request.getBasicSalary());
        payroll.setAllowances(allowances);
        payroll.setDeductions(deductions);
        payroll.setNetPay(netPay);
        payroll.setPaymentDate(LocalDate.now()); // Set current systemic date timestamp

        return mapToResponse(payrollRepository.save(payroll));
    }

    @Override public List<PayrollResponse> getPayrollHistoryByEmployee(UUID id) { return List.of(); }

    private PayrollResponse mapToResponse(Payroll entity) {
        PayrollResponse res = new PayrollResponse();
        res.setId(entity.getId());
        res.setEmployeeId(entity.getEmployeeId());
        res.setPayPeriodStart(entity.getPayPeriodStart());
        res.setPayPeriodEnd(entity.getPayPeriodEnd());
        res.setBasicSalary(entity.getBasicSalary());
        res.setAllowances(entity.getAllowances());
        res.setDeductions(entity.getDeductions());
        res.setNetPay(entity.getNetPay());
        res.setPaymentDate(entity.getPaymentDate());
        return res;
    }
}
