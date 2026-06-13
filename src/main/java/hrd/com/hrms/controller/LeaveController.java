package hrd.com.hrms.controller;

import hrd.com.hrms.common.ApiResponse;
import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<LeaveResponse>> requestLeave(
            @Valid @RequestPart("data") LeaveRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        LeaveResponse response = leaveService.createLeaveRequest(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Leave request submitted successfully", response));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<LeaveResponse>> processStatus(
            @PathVariable UUID id,
            @RequestParam LeaveStatus status) {
        LeaveResponse response = leaveService.updateLeaveStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Leave status updated", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Page<LeaveResponse>>> getAllLogs(
            @RequestParam(required = false) LeaveStatus status, Pageable pageable) {
        Page<LeaveResponse> records = (status != null)
                ? leaveService.getLeavesByStatus(status, pageable)
                : leaveService.getAllLeaveRequests(pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Administrative records loaded", records));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeaveResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Found", leaveService.getLeaveById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<Page<LeaveResponse>>> getPersonalLogs(@PathVariable UUID employeeId, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "History loaded", leaveService.getEmployeeLeaveHistory(employeeId, pageable)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Deleted successfully", null));
    }
}