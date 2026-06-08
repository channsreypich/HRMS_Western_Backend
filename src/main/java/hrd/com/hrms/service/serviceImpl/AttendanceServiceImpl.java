package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.AttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.exception.BadRequestException;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.mapper.AttendanceMapper;
import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.repository.AttendanceRepository;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.service.AttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 EmployeeRepository employeeRepository,
                                 AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Override
    public AttendanceResponse processScan(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee record not found with UUID: " + request.getEmployeeId()));

        LocalDate today = LocalDate.now();
        Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeAndDate(employee, today);

        Attendance trackingRecord;

        if (existingAttendance.isEmpty()) {
            // Smart Logic: First scan of the day -> CHECK-IN
            trackingRecord = Attendance.builder()
                    .employee(employee)
                    .date(today)
                    .checkIn(LocalDateTime.now())
                    .status(AttendanceStatus.PRESENT) // You can inject cutoff logic here for LATE status
                    .scanType(request.getScanType())
                    .build();
        } else {
            // Smart Logic: Second scan of the day -> CHECK-OUT
            trackingRecord = existingAttendance.get();
            if (trackingRecord.getCheckOut() != null) {
                throw new BadRequestException("Employee has already checked out for today.");
            }
            trackingRecord.setCheckOut(LocalDateTime.now());
        }

        Attendance savedRecord = attendanceRepository.save(trackingRecord);
        return attendanceMapper.toResponse(savedRecord);
    }

    @Override
    public AttendanceResponse getAttendanceById(UUID id) {
        return attendanceRepository.findById(id)
                .map(attendanceMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
    }

    @Override
    public Page<AttendanceResponse> getEmployeeAttendanceHistory(UUID employeeId, Pageable pageable) {
        return attendanceRepository.findByEmployeeId(employeeId, pageable)
                .map(attendanceMapper::toResponse);
    }

    @Override
    public Page<AttendanceResponse> getAllAttendanceLogs(Pageable pageable) {
        return attendanceRepository.findAll(pageable)
                .map(attendanceMapper::toResponse);
    }

    @Override
    public void deleteAttendance(UUID id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
        attendanceRepository.delete(attendance);
    }
}