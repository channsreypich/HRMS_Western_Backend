package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.AttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface AttendanceService {
    AttendanceResponse clockIn(AttendanceRequest request);
    AttendanceResponse clockOut(UUID id, AttendanceRequest request);
    AttendanceResponse getAttendanceById(UUID id);
    List<AttendanceResponse> getEmployeeAttendanceHistory(UUID employeeId);

    AttendanceResponse recordAttendance(@Valid AttendanceRequest request);
}