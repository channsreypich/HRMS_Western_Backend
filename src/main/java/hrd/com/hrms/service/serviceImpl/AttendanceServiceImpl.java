package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.exception.ResourceNotFoundException;
import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.model.Employee;
import hrd.com.hrms.repository.AttendanceRepository;
import hrd.com.hrms.repository.EmployeeRepository;
import hrd.com.hrms.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendanceRecords() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToAttendanceResponse)
                .toList();
    }

    @Override
    @Transactional
    public AttendanceResponse processScanAttendance(String employeeCode, String scanType, MultipartFile selfie) {
        // 1. Locate the employee using their unique Employee Code (e.g., EMP001) sent from Vue local storage
        // Assumes your EmployeeRepository contains a custom finder method: findByEmployeeCode(String code)
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + employeeCode));

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Optional: If you want to handle saving the image to a cloud or local folder structure:
        if (selfie != null && !selfie.isEmpty()) {
            // String fileUrl = fileStorageService.save(selfie);
            // You can record this URL on your attendance domain entity if needed!
        }

        Attendance attendance;

        // 2. Handle Check-In / Check-Out logic based on context route type
        if ("OUT".equalsIgnoreCase(scanType)) {
            // Find today's existing check-in record to update with a check-out time
            attendance = attendanceRepository.findByEmployeeAndDate(employee, today)
                    .orElseThrow(() -> new ResourceNotFoundException("No check-in record found for this employee today."));

            attendance.setCheckOut(now);
        } else {
            // Default to "IN" scan: Create a fresh attendance log entry for today

            // Check if they are late (comparing against 08:15 AM today)
            LocalDateTime lateThreshold = today.atTime(8, 15);
            AttendanceStatus status = now.isAfter(lateThreshold) ? AttendanceStatus.LATE : AttendanceStatus.PRESENT;

            attendance = Attendance.builder()
                    .employee(employee)
                    .date(today)
                    .checkIn(now)
                    .status(status)
                    .build();
        }

        Attendance savedRecord = attendanceRepository.save(attendance);

        // Pass context tracking back out to mapping so DTO accurately reflects the operation done
        AttendanceResponse response = mapToAttendanceResponse(savedRecord);
        response.setScanType(scanType.toUpperCase());
        return response;
    }

    private AttendanceResponse mapToAttendanceResponse(Attendance attendance) {
        Employee emp = attendance.getEmployee();

        // 1. Safely combine first name and last name into a single employeeName String
        String fullName = "Unknown Employee";
        if (emp != null) {
            String firstName = emp.getFirstName() != null ? emp.getFirstName() : "";
            String lastName = emp.getLastName() != null ? emp.getLastName() : "";
            fullName = (firstName + " " + lastName).trim();
            if (fullName.isEmpty()) {
                fullName = "Unknown Employee";
            }
        }

        // 2. Use the Lombok Builder to match the specific DTO types (LocalDateTime directly)
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(emp != null ? emp.getId() : null)
                .employeeName(fullName)
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .status(attendance.getStatus() != null ? attendance.getStatus() : AttendanceStatus.PRESENT)
                .build();
    }
}