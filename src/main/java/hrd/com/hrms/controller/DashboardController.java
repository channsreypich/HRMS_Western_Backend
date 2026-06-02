package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardSummary() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalEmployees", 120);
        statistics.put("activeLeavesToday", 4);
        statistics.put("pendingLeaveRequests", 8);
        statistics.put("attendanceRateToday", "94.5%");

        return ResponseEntity.ok(new ApiResponse<>("Dashboard analytical context populated", statistics, true, java.time.LocalDateTime.now()));
    }
}