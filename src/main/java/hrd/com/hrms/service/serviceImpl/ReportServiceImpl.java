package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.repository.*;
import hrd.com.hrms.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EmployeeRepository employeeRepository;
    private final PayrollRepository payrollRepository;
    private final LeaveRepository leaveRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public List<Map<String, Object>> getHeadcountByDepartment() {
        return employeeRepository.countEmployeesByDepartment();
    }

    @Override
    public List<Map<String, Object>> getPayrollExpenditureReport() {
        return payrollRepository.getMonthlyPayrollExpenditure();
    }

    @Override
    public List<Map<String, Object>> getLeaveReport() {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (LeaveStatus status : LeaveStatus.values()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("status", status.name());
            row.put("count", leaveRepository.countByStatus(status));
            rows.add(row);
        }
        return rows;
    }

    @Override
    public List<Map<String, Object>> getTurnoverReport() {
        // Hires grouped by calendar month (separations aren't tracked, so this reflects onboarding trend)
        Map<String, Long> hiresByMonth = new TreeMap<>();
        for (Employee e : employeeRepository.findAll()) {
            if (e.getHireDate() == null) continue;
            String month = e.getHireDate().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                    + " " + e.getHireDate().getYear();
            hiresByMonth.merge(month, 1L, Long::sum);
        }
        return hiresByMonth.entrySet().stream()
                .map(en -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("month", en.getKey());
                    row.put("hires", en.getValue());
                    return row;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getPerformanceReport() {
        // Attendance status distribution as a simple performance proxy
        List<Attendance> all = attendanceRepository.findAll();
        long total = all.size();
        List<Map<String, Object>> rows = new ArrayList<>();
        for (AttendanceStatus status : AttendanceStatus.values()) {
            long count = all.stream().filter(a -> a.getStatus() == status).count();
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("status", status.name());
            row.put("count", count);
            row.put("percentage", total == 0 ? 0 : Math.round((count * 100.0) / total));
            rows.add(row);
        }
        return rows;
    }
}