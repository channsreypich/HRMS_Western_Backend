package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.AttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/scan")
    public ResponseEntity<ApiResponse<AttendanceResponse>> scanAttendance(@RequestBody AttendanceRequest request) {
        AttendanceResponse response = attendanceService.processScan(request);
        String message = (response.getCheckOut() == null) ? "Check-in successful" : "Check-out successful";

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), message, response));
    }

    @GetMapping("/history/{employeeId}")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getHistory(
            @PathVariable UUID employeeId, Pageable pageable) {
        Page<AttendanceResponse> history = attendanceService.getEmployeeAttendanceHistory(employeeId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched employee logs successfully", history));
    }

    @GetMapping("/logs")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Page<AttendanceResponse>>> getAllLogs(Pageable pageable) {
        Page<AttendanceResponse> logs = attendanceService.getAllAttendanceLogs(pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Fetched all management system logs", logs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Attendance record found", attendanceService.getAttendanceById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Attendance record deleted successfully", null));
    }
}