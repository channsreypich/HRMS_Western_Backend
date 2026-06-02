package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.AttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.repository.AttendanceRepository;
import hrd.com.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Override
    @Transactional
    public AttendanceResponse clockIn(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setId(UUID.randomUUID());
        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setClockInTime(LocalDateTime.now());
        attendance.setStatus("PRESENT");

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public AttendanceResponse clockOut(UUID id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record missing"));
        attendance.setClockOutTime(LocalDateTime.now());

        Attendance updated = attendanceRepository.save(attendance);
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponse getAttendanceById(UUID id) {
        return attendanceRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Attendance record missing"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getEmployeeAttendanceHistory(UUID employeeId) {
        // In production, add a custom query to your repository layer to filter by employeeId
        return attendanceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceResponse recordAttendance(AttendanceRequest request) {
        return null;
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployeeId());
        response.setClockInTime(attendance.getClockInTime());
        response.setClockOutTime(attendance.getClockOutTime());
        response.setStatus(attendance.getStatus());
        return response;
    }
}