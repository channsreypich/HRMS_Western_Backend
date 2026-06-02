package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.model.Leave;
import hrd.com.hrms.repository.LeaveRepository;
import hrd.com.hrms.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;

    @Override
    public LeaveResponse requestLeave(LeaveRequest request) {
        return null;
    }

    @Override
    public LeaveResponse updateLeaveStatus(UUID id, String status) {
        return null;
    }

    @Override
    public List<LeaveResponse> getEmployeeLeaveHistory(UUID employeeId) {
        return List.of();
    }

    @Override
    public List<LeaveResponse> getPendingLeaveRequests() {
        return List.of();
    }

    @Override
    public LeaveResponse applyLeave(hrd.com.hrms.dto.request.LeaveRequest request) {
        Leave leave = new Leave();
        leave.setId(UUID.randomUUID());
        leave.setEmployeeId(request.getEmployeeId());
        leave.setLeaveType(request.getLeaveType());
        leave.setStartDate(request.getStartDate());
        leave.setEndDate(request.getEndDate());
        leave.setReason(request.getReason());
        leave.setStatus("PENDING");
        return mapToResponse(leaveRepository.save(leave));
    }

    @Override
    public LeaveResponse updateStatus(UUID leaveId, String status) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Request target reference missing"));
        leave.setStatus(status);
        return mapToResponse(leaveRepository.save(leave));
    }

    @Override
    public List<LeaveResponse> getAllLeaveRequests() {
        return leaveRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private LeaveResponse mapToResponse(Leave model) {
        LeaveResponse res = new LeaveResponse();
        res.setId(model.getId());
        res.setEmployeeId(model.getEmployeeId());
        res.setLeaveType(model.getLeaveType());
        res.setStartDate(model.getStartDate());
        res.setEndDate(model.getEndDate());
        res.setReason(model.getReason());
        res.setStatus(model.getStatus());
        return res;
    }
}