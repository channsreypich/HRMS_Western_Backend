package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReportController {

    @GetMapping("/attendance")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAttendanceReport(@RequestParam String month) {
        List<Map<String, Object>> records = new ArrayList<>();
        Map<String, Object> mockRecord = new HashMap<>();
        mockRecord.put("month", month);
        mockRecord.put("totalWorkingDays", 22);
        mockRecord.put("averagePresentCount", 112);
        records.add(mockRecord);

        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Attendance summary timeline report compiled", records));
    }
}