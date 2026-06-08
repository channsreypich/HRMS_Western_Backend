package hrd.com.hrms.service;

import hrd.com.hrms.dto.response.AttendanceResponse;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AttendanceService {
    List<AttendanceResponse> getAllAttendanceRecords();

    AttendanceResponse processScanAttendance(String employeeCode, String scanType, MultipartFile selfie);
}