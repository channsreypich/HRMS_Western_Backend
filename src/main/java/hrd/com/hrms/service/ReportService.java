package hrd.com.hrms.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> getHeadcountByDepartment();
    List<Map<String, Object>> getPayrollExpenditureReport();
    List<Map<String, Object>> getLeaveReport();
    List<Map<String, Object>> getTurnoverReport();
    List<Map<String, Object>> getPerformanceReport();
}