package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.response.DashboardMetricsResponse;
import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.repository.AttendanceRepository;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.repository.LeaveRepository;
import hrd.com.hrms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import hrd.com.hrms.model.Employee;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRepository leaveRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsResponse getSummaryMetrics() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate firstOfThisMonth = today.withDayOfMonth(1);
        LocalDate lastMonthEnd = firstOfThisMonth.minusDays(1); // last day of previous month
        LocalDateTime now = LocalDateTime.now();

        // 1. Current live counts straight from the database
        List<Employee> allEmployees = employeeRepository.findAll();
        long totalEmployees = allEmployees.size();
        long activeEmployees = allEmployees.stream().filter(this::isActive).count();
        long pendingLeaves = leaveRepository.countByStatus(LeaveStatus.PENDING);
        long presentToday = countPresent(today);

        // 2. Baselines for honest "vs last month" trends, derived from real date columns
        // Workforce/active as they stood at the end of last month (by hire date)
        long workforceLastMonth = allEmployees.stream()
                .filter(e -> e.getHireDate() != null && !e.getHireDate().isAfter(lastMonthEnd))
                .count();
        long activeLastMonth = allEmployees.stream()
                .filter(this::isActive)
                .filter(e -> e.getHireDate() != null && !e.getHireDate().isAfter(lastMonthEnd))
                .count();

        // Pending leaves raised this month vs the same window last month (by createdAt)
        LocalDateTime thisMonthStart = firstOfThisMonth.atStartOfDay();
        LocalDateTime lastMonthStart = firstOfThisMonth.minusMonths(1).atStartOfDay();
        long pendingThisMonth = leaveRepository.countByStatusAndCreatedAtBetween(
                LeaveStatus.PENDING, thisMonthStart, now);
        long pendingLastMonth = leaveRepository.countByStatusAndCreatedAtBetween(
                LeaveStatus.PENDING, lastMonthStart, thisMonthStart);

        // Attendance: today's turnout vs yesterday's
        long presentYesterday = countPresent(yesterday);

        // 3. Build response with real values and real, DB-derived trends
        return DashboardMetricsResponse.builder()
                .totalWorkforce(totalEmployees)
                .activeStaff(activeEmployees)
                .pendingLeaves(pendingLeaves)
                .presentToday(presentToday)
                .totalWorkforceTrend(percentChange(totalEmployees, workforceLastMonth))
                .activeStaffTrend(percentChange(activeEmployees, activeLastMonth))
                .pendingLeavesTrend(percentChange(pendingThisMonth, pendingLastMonth))
                .presentTodayTrend(percentChange(presentToday, presentYesterday))
                .build();
    }

    // Employees with status "active" (a null status is treated as active)
    private boolean isActive(Employee e) {
        return e.getStatus() == null || "active".equalsIgnoreCase(e.getStatus());
    }

    // Present headcount for a day = those marked PRESENT plus those marked LATE
    private long countPresent(LocalDate date) {
        return attendanceRepository.countByDateAndStatus(date, AttendanceStatus.PRESENT)
                + attendanceRepository.countByDateAndStatus(date, AttendanceStatus.LATE);
    }

    // Whole-percent change from a previous value to the current one.
    // When there was nothing before, a positive current counts as +100%.
    private int percentChange(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? 100 : 0;
        }
        return Math.round((current - previous) * 100f / previous);
    }
}