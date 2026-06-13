package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.ManualAttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface AttendanceService {
    List<AttendanceResponse> getAllAttendanceRecords();

    List<AttendanceResponse> getEmployeeAttendanceHistory(UUID employeeId);

    AttendanceResponse processScanAttendance(String employeeCode, String scanType, MultipartFile selfie, String faceDescriptor);

    AttendanceResponse recordManualAttendance(ManualAttendanceRequest request);

    void enrollFace(java.util.UUID employeeId, String descriptor);

    void resetFace(java.util.UUID employeeId);
}