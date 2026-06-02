package hrd.com.hrms.service;

import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper
public interface LeaveService {
    LeaveResponse requestLeave(LeaveRequest request);
    LeaveResponse updateLeaveStatus(UUID id, String status);
    List<LeaveResponse> getEmployeeLeaveHistory(UUID employeeId);
    List<LeaveResponse> getPendingLeaveRequests();

    LeaveResponse applyLeave(@Valid LeaveRequest request);

    LeaveResponse updateStatus(UUID id, String status);

    List<LeaveResponse> getAllLeaveRequests();
}