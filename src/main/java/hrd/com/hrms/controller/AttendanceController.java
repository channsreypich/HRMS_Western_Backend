package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.ManualAttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> fetchAllAttendance() {
        List<AttendanceResponse> records = attendanceService.getAllAttendanceRecords();
        return ResponseEntity.ok(ApiResponse.success(records, "Attendance logs compiled successfully"));
    }

    @GetMapping("/history/{employeeId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> fetchEmployeeHistory(@PathVariable UUID employeeId) {
        List<AttendanceResponse> records = attendanceService.getEmployeeAttendanceHistory(employeeId);
        return ResponseEntity.ok(ApiResponse.success(records, "Employee attendance history loaded successfully"));
    }

    @PostMapping("/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponse>> registerCheckIn(
            @RequestParam("employee_code") String employeeCode,
            @RequestParam(value = "selfie", required = false) MultipartFile selfie,
            @RequestParam(value = "face_descriptor", required = false) String faceDescriptor) {

        AttendanceResponse response = attendanceService.processScanAttendance(employeeCode, "IN", selfie, faceDescriptor);
        return ResponseEntity.ok(ApiResponse.success(response, "Check-in action recorded successfully"));
    }

    @PostMapping("/check-out")
    public ResponseEntity<ApiResponse<AttendanceResponse>> registerCheckOut(
            @RequestParam("employee_code") String employeeCode,
            @RequestParam(value = "selfie", required = false) MultipartFile selfie,
            @RequestParam(value = "face_descriptor", required = false) String faceDescriptor) {

        AttendanceResponse response = attendanceService.processScanAttendance(employeeCode, "OUT", selfie, faceDescriptor);
        return ResponseEntity.ok(ApiResponse.success(response, "Check-out action recorded successfully"));
    }

    @PostMapping("/manual")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<AttendanceResponse>> recordManual(@RequestBody ManualAttendanceRequest request) {
        AttendanceResponse response = attendanceService.recordManualAttendance(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Attendance record saved successfully"));
    }

    // HR can manually (re)enroll an employee's face descriptor
    @PostMapping("/face/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> enrollFace(
            @PathVariable java.util.UUID employeeId,
            @RequestBody java.util.Map<String, String> body) {
        attendanceService.enrollFace(employeeId, body.get("descriptor"));
        return ResponseEntity.ok(ApiResponse.success(null, "Face enrolled successfully"));
    }

    // HR can clear an employee's face so it re-enrolls on the next scan
    @DeleteMapping("/face/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> resetFace(@PathVariable java.util.UUID employeeId) {
        attendanceService.resetFace(employeeId);
        return ResponseEntity.ok(ApiResponse.success(null, "Face enrollment reset. The next scan will re-enroll."));
    }
}