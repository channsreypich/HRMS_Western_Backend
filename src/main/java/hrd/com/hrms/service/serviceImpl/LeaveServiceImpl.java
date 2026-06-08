package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.LeaveRequest;
import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.enums.LeaveStatus;
import hrd.com.hrms.exception.BadRequestException;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.mapper.LeaveMapper;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.model.Leave;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.repository.LeaveRepository;
import hrd.com.hrms.service.FileStorageService;
import hrd.com.hrms.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;
    private final FileStorageService fileStorageService;

    @Override
    public LeaveResponse createLeaveRequest(LeaveRequest request, MultipartFile file) {
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date cannot occur before start date.");
        }

        // Entity Mapping
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee record not found."));
        String documentPath = null;
        try {
            documentPath = fileStorageService.storeFile(file);
        } catch (IOException e) {
            throw new BadRequestException("Could not store file: " + e.getMessage());
        }

        Leave leave = Leave.builder()
                .employee(employee)
                .leaveType(request.getLeaveType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .documentPath(documentPath)
                .build();

        return leaveMapper.toResponse(leaveRepository.save(leave));
    }

    @Override
    public LeaveResponse updateLeaveStatus(UUID id, LeaveStatus status) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found matching id: " + id));

        leave.setStatus(status);
        return leaveMapper.toResponse(leaveRepository.save(leave));
    }

    @Override
    public LeaveResponse getLeaveById(UUID id) {
        return leaveRepository.findById(id)
                .map(leaveMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found matching id: " + id));
    }

    @Override
    public Page<LeaveResponse> getAllLeaveRequests(Pageable pageable) {
        return leaveRepository.findAll(pageable).map(leaveMapper::toResponse);
    }

    @Override
    public Page<LeaveResponse> getLeavesByStatus(LeaveStatus status, Pageable pageable) {
        return leaveRepository.findByStatus(status, pageable).map(leaveMapper::toResponse);
    }

    @Override
    public Page<LeaveResponse> getEmployeeLeaveHistory(UUID employeeId, Pageable pageable) {
        return leaveRepository.findByEmployeeId(employeeId, pageable).map(leaveMapper::toResponse);
    }

    @Override
    public void deleteLeave(UUID id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found matching id: " + id));
        leaveRepository.delete(leave);
    }
}