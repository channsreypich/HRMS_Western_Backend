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
                .orElseThrow(() -> new ResourceNotFoundException("Target employee profile for payroll calculation not found."));

        // Net Pay formula computation
        double calculatedNetPay = (request.getBasicSalary() + request.getAllowances()) - request.getDeductions();

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .paymentDate(LocalDate.now())
                .basicSalary(request.getBasicSalary())
                .allowances(request.getAllowances())
                .deductions(request.getDeductions())
                .netPay(calculatedNetPay)
                .build();

        return payrollMapper.toResponse(payrollRepository.save(payroll));
    }

    @Override
    public PayrollResponse getPayrollById(UUID id) {
        return payrollRepository.findById(id)
                .map(payrollMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("Payroll record not found with id: " + id));
        payrollRepository.delete(payroll);
    }
}