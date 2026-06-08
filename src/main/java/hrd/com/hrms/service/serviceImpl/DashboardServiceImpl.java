package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.response.DashboardMetricsResponse;
import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.repository.AttendanceRepository;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    // Inject LeaveRepository here when ready:
    // private final LeaveRepository leaveRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsResponse getSummaryMetrics() {
        LocalDate today = LocalDate.now();

        // 1. Get raw database counts efficiently
        long totalEmployees = employeeRepository.count();

        // Mocking or adjusting based on your active status schemas
        long activeEmployees = totalEmployees;

        long pendingLeaves = 3; // Replace with leaveRepository.countByStatus("PENDING")

        // Count employees who scanned today with a non-null context status
        long presentToday = attendanceRepository.countByDateAndStatus(today, AttendanceStatus.PRESENT)
                + attendanceRepository.countByDateAndStatus(today, AttendanceStatus.LATE);

        // 2. Build response payload returning exact matching data properties
        return DashboardMetricsResponse.builder()
                .totalWorkforce(totalEmployees)
                .activeStaff(activeEmployees)
                .pendingLeaves(pendingLeaves)
                .presentToday(presentToday)
                .totalWorkforceTrend(12)  // Hardcoded placeholders or computed targets
                .activeStaffTrend(8)
                .pendingLeavesTrend(-4)
                .presentTodayTrend(15)
                .build();
    }
}