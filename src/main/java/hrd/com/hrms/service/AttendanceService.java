package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.AttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface AttendanceService {
    AttendanceResponse processScan(AttendanceRequest request);
    AttendanceResponse getAttendanceById(UUID id);
    Page<AttendanceResponse> getEmployeeAttendanceHistory(UUID employeeId, Pageable pageable);
    Page<AttendanceResponse> getAllAttendanceLogs(Pageable pageable);
    void deleteAttendance(UUID id);
}