package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> fetchAllAttendance() {
        List<AttendanceResponse> records = attendanceService.getAllAttendanceRecords();
        return ResponseEntity.ok(ApiResponse.success(records, "Attendance logs compiled successfully"));
    }

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponse>> registerCheckIn(
            @RequestParam("employee_code") String employeeCode,
            @RequestParam(value = "selfie", required = false) MultipartFile selfie) {

        AttendanceResponse response = attendanceService.processScanAttendance(employeeCode, "IN", selfie);
        return ResponseEntity.ok(ApiResponse.success(response, "Check-in action recorded successfully"));
    }

    @PostMapping("/check-out")
    public ResponseEntity<ApiResponse<AttendanceResponse>> registerCheckOut(
            @RequestParam("employee_code") String employeeCode,
            @RequestParam(value = "selfie", required = false) MultipartFile selfie) {

        AttendanceResponse response = attendanceService.processScanAttendance(employeeCode, "OUT", selfie);
        return ResponseEntity.ok(ApiResponse.success(response, "Check-out action recorded successfully"));
    }
}