package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<LeaveResponse>> requestLeave(@Valid @RequestBody LeaveRequest request) {
        LeaveResponse response = leaveService.createLeaveRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Leave request submitted successfully", response));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<LeaveResponse>> processStatus(
            @PathVariable UUID id,
            @RequestParam LeaveStatus status) {
        LeaveResponse response = leaveService.updateLeaveStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Leave status modified to " + status, response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Page<LeaveResponse>>> getAllLogs(
            @RequestParam(required = false) LeaveStatus status, Pageable pageable) {
        Page<LeaveResponse> records = (status != null)
                ? leaveService.getLeavesByStatus(status, pageable)
                : leaveService.getAllLeaveRequests(pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "All administrative leave records loaded", records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Leave request found", leaveService.getLeaveById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<Page<LeaveResponse>>> getPersonalLogs(@PathVariable UUID employeeId, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Personal leave history loaded", leaveService.getEmployeeLeaveHistory(employeeId, pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Leave request deleted successfully", null));
    }
}