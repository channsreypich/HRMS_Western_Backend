package hrd.com.hrms.controller;

import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.service.LeaveService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    @PostMapping
    public ResponseEntity<LeaveResponse> submitRequest(@Valid @RequestBody LeaveRequest request) {
        return ResponseEntity.ok(leaveService.applyLeave(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LeaveResponse> updateStatus(@PathVariable UUID id, @RequestParam String status) {
        return ResponseEntity.ok(leaveService.updateStatus(id, status));
    }

    @GetMapping
    public ResponseEntity<List<LeaveResponse>> fetchAll() {
        return ResponseEntity.ok(leaveService.getAllLeaveRequests());
    }
}