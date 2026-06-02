package hrd.com.hrms.controller;

import hrd.com.hrms.dto.request.AttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping("/scan")
    public ResponseEntity<AttendanceResponse> scanAttendance(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.ok(attendanceService.recordAttendance(request));
    }
}