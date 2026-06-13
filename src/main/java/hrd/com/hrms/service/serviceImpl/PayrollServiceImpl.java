package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.PayrollRequest;
import hrd.com.hrms.dto.response.PayrollResponse;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.mapper.PayrollMapper;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.model.Payroll;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.repository.PayrollRepository;
import hrd.com.hrms.service.PayrollService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hrd.com.hrms.exception.BadRequestException;
import hrd.com.hrms.model.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Service
@Transactional
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;
    private final PayrollMapper payrollMapper;

    public PayrollServiceImpl(PayrollRepository payrollRepository, EmployeeRepository employeeRepository, PayrollMapper payrollMapper) {
        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
        this.payrollMapper = payrollMapper;
    }

    @Override
    public PayrollResponse calculateAndSavePayroll(PayrollRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));

        // Convert request values to BigDecimal for accurate financial calculation
        BigDecimal basic = BigDecimal.valueOf(request.getBasicSalary());
        BigDecimal allow = BigDecimal.valueOf(request.getAllowances());
        BigDecimal deduc = BigDecimal.valueOf(request.getDeductions());

        // Net Pay = (Basic + Allowances) - Deductions
        BigDecimal netPay = basic.add(allow).subtract(deduc);

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .paymentDate(LocalDate.now())
                .basicSalary(basic)
                .allowances(allow)
                .deductions(deduc)
                .netPay(netPay)
                .status("draft")
                .build();

        return payrollMapper.toResponse(payrollRepository.save(payroll));
    }

    @Override
    public int generateMonthlyPayroll(String paymentMonth) {
        YearMonth ym;
        try {
            ym = YearMonth.parse(paymentMonth); // expects "YYYY-MM"
        } catch (DateTimeParseException e) {
            throw new BadRequestException("payment_month must be in 'YYYY-MM' format, e.g. 2026-06");
        }

        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        int generated = 0;

        for (Employee employee : employeeRepository.findAll()) {
            // Only active employees, and never duplicate a record already paid that month
            boolean active = employee.getStatus() == null || "active".equalsIgnoreCase(employee.getStatus());
            if (!active) continue;
            if (payrollRepository.existsByEmployeeIdAndPaymentDateBetween(employee.getId(), start, end)) continue;

            BigDecimal basic = resolveBasicSalary(employee);
            BigDecimal allowances = BigDecimal.ZERO;
            BigDecimal deductions = BigDecimal.ZERO;
            BigDecimal netPay = basic.add(allowances).subtract(deductions);

            Payroll payroll = Payroll.builder()
                    .employee(employee)
                    .payPeriodStart(start)
                    .payPeriodEnd(end)
                    .paymentDate(end)
                    .basicSalary(basic)
                    .allowances(allowances)
                    .deductions(deductions)
                    .netPay(netPay)
                    .status("draft")
                    .build();

            payrollRepository.save(payroll);
            generated++;
        }
        return generated;
    }

    // Prefer the employee's own base salary, fall back to their position's base salary, else zero
    private BigDecimal resolveBasicSalary(Employee employee) {
        if (employee.getBaseSalary() != null) return employee.getBaseSalary();
        Position pos = employee.getPosition();
        if (pos != null && pos.getBaseSalary() != null) return pos.getBaseSalary();
        return BigDecimal.ZERO;
    }

    @Override
    public PayrollResponse updatePayrollStatus(UUID id, String status) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found."));
        payroll.setStatus(status.toLowerCase());
        return payrollMapper.toResponse(payrollRepository.save(payroll));
    }

    @Override
    public PayrollResponse getPayrollById(UUID id) {
        return payrollRepository.findById(id)
                .map(payrollMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found."));
    }

    @Override
    public Page<PayrollResponse> getEmployeePayrollHistory(UUID employeeId, Pageable pageable) {
        return payrollRepository.findByEmployeeId(employeeId, pageable).map(payrollMapper::toResponse);
    }

    @Override
    public Page<PayrollResponse> getAllPayrollRecords(Pageable pageable) {
        return payrollRepository.findAll(pageable).map(payrollMapper::toResponse);
    }

    @Override
    public void deletePayroll(UUID id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found."));
        payrollRepository.delete(payroll);
    }
}