package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface LeaveService {
    LeaveResponse createLeaveRequest(LeaveRequest request, MultipartFile file);
    LeaveResponse updateLeaveStatus(UUID id, LeaveStatus status);
    LeaveResponse getLeaveById(UUID id);
    Page<LeaveResponse> getAllLeaveRequests(Pageable pageable);
    Page<LeaveResponse> getLeavesByStatus(LeaveStatus status, Pageable pageable);
    Page<LeaveResponse> getEmployeeLeaveHistory(UUID employeeId, Pageable pageable);
    void deleteLeave(UUID id);
}