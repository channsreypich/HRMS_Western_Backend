package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.repository.AttendanceRepository;
import hrd.com.hrms.repository.DepartmentRepository;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.repository.LeaveRepository;
import hrd.com.hrms.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardServiceImpl(EmployeeRepository employeeRepository,
                                AttendanceRepository attendanceRepository,
                                LeaveRepository leaveRepository,
                                DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRepository = leaveRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Map<String, Object> getMetricsSummary() {
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("totalEmployees", employeeRepository.count());
        metrics.put("todayPresentCount", attendanceRepository.countByDateAndCheckInIsNotNull(LocalDate.now()));
        metrics.put("pendingLeaveRequests", leaveRepository.countByStatus(LeaveStatus.PENDING));
        metrics.put("totalDepartments", departmentRepository.count());

        return metrics;
    }
}