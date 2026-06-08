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
import java.math.BigDecimal;
import java.time.LocalDate;
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
                .build();

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