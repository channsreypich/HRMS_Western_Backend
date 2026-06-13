package hrd.com.hrms.service.serviceImpl;

import hrd.com.hrms.dto.request.ManualAttendanceRequest;
import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.enums.AttendanceStatus;
import hrd.com.hrms.exception.BadRequestException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final hrd.com.hrms.service.FileStorageService fileStorageService;

    // Office work-start time; arrivals after this are flagged LATE
    private static final int WORK_START_HOUR = 9;
    private static final int WORK_START_MINUTE = 0;

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendanceRecords() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToAttendanceResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getEmployeeAttendanceHistory(UUID employeeId) {
        return attendanceRepository.findByEmployeeIdOrderByDateDesc(employeeId).stream()
                .map(this::mapToAttendanceResponse)
                .toList();
    }

    @Override
    @Transactional
    public AttendanceResponse processScanAttendance(String employeeCode, String scanType, MultipartFile selfie, String faceDescriptor) {
        // 1. Locate the employee by their code, tolerating common typing variants (case, spacing, missing dash)
        Employee employee = findEmployeeByCodeFlexible(employeeCode);

        // 1b. Face verification — the employee's face must be enrolled by HR first, then must match
        verifyFace(employee, faceDescriptor);

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Reuse today's record if one exists (avoids duplicate rows per day)
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, today)
                .orElseGet(() -> Attendance.builder()
                        .employee(employee)
                        .date(today)
                        .status(AttendanceStatus.PRESENT)
                        .build());

        // 2. Handle Check-In / Check-Out logic based on context route type
        if ("OUT".equalsIgnoreCase(scanType)) {
            // Auto-create the day's record if the employee never checked in
            attendance.setCheckOut(now);
            if (attendance.getStatus() == null) {
                attendance.setStatus(AttendanceStatus.PRESENT);
            }
        } else {
            // Check-in: stamp arrival time and flag late if after the configured 09:00 start
            LocalDateTime lateThreshold = today.atTime(WORK_START_HOUR, WORK_START_MINUTE);
            attendance.setCheckIn(now);
            attendance.setStatus(now.isAfter(lateThreshold) ? AttendanceStatus.LATE : AttendanceStatus.PRESENT);
        }

        // Persist the selfie proof when provided
        if (selfie != null && !selfie.isEmpty()) {
            try {
                attendance.setSelfieUrl(fileStorageService.storeFile(selfie));
            } catch (java.io.IOException e) {
                throw new BadRequestException("Could not store selfie: " + e.getMessage());
            }
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
                .firstName(emp != null ? emp.getFirstName() : null)
                .lastName(emp != null ? emp.getLastName() : null)
                .employeeCode(emp != null ? emp.getEmployeeCode() : null)
                .departmentName(emp != null && emp.getDepartment() != null ? emp.getDepartment().getName() : null)
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .status(attendance.getStatus() != null ? attendance.getStatus() : AttendanceStatus.PRESENT)
                .scanType(attendance.getScanType())
                .selfieUrl(attendance.getSelfieUrl())
                .build();
    }

    @Override
    @Transactional
    public AttendanceResponse recordManualAttendance(ManualAttendanceRequest request) {
        Employee employee = resolveEmployee(request);

        LocalDate date = request.getDate() != null ? request.getDate() : LocalDate.now();

        // Reuse an existing record for that employee/day, otherwise create a new one
        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, date)
                .orElseGet(() -> Attendance.builder().employee(employee).date(date).build());

        attendance.setDate(date);
        attendance.setCheckIn(parseTime(date, request.getCheckIn()));
        attendance.setCheckOut(parseTime(date, request.getCheckOut()));

        AttendanceStatus status;
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            try {
                status = AttendanceStatus.valueOf(request.getStatus().trim().toUpperCase().replace(' ', '_'));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid attendance status: " + request.getStatus());
            }
        } else {
            status = AttendanceStatus.PRESENT;
        }
        attendance.setStatus(status);
        attendance.setScanType("MANUAL");

        return mapToAttendanceResponse(attendanceRepository.save(attendance));
    }

    // Max Euclidean distance for two descriptors to be considered the same person (face-api.js default)
    private static final double FACE_MATCH_THRESHOLD = 0.55;

    // Verifies the scanned face against the face HR enrolled for this employee.
    // Enrollment is HR-only: an employee with no enrolled face cannot scan until HR registers them.
    private void verifyFace(Employee employee, String incomingDescriptor) {
        boolean hasStored = employee.getFaceDescriptor() != null && !employee.getFaceDescriptor().isBlank();
        if (!hasStored) {
            throw new BadRequestException(
                    "Face not enrolled for " + employee.getEmployeeCode() +
                    ". Please ask HR to register your face before scanning attendance.");
        }

        double[] incoming = parseDescriptor(incomingDescriptor);
        if (incoming == null) {
            throw new BadRequestException("Face scan required. Please capture your face with the camera to verify your identity.");
        }

        double[] stored = parseDescriptor(employee.getFaceDescriptor());
        double distance = euclideanDistance(incoming, stored);
        if (distance > FACE_MATCH_THRESHOLD) {
            throw new BadRequestException(
                    "Face does not match the registered employee (" + employee.getEmployeeCode() +
                    "). Please try again in good lighting, or ask HR to re-enroll your face.");
        }
    }

    private double[] parseDescriptor(String csv) {
        if (csv == null || csv.isBlank()) return null;
        String[] parts = csv.split(",");
        if (parts.length < 64) return null; // not a valid descriptor
        double[] out = new double[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) out[i] = Double.parseDouble(parts[i].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return out;
    }

    private String serializeDescriptor(double[] d) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < d.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(d[i]);
        }
        return sb.toString();
    }

    private double euclideanDistance(double[] a, double[] b) {
        int n = Math.min(a.length, b.length);
        double sum = 0;
        for (int i = 0; i < n; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    @Override
    @Transactional
    public void enrollFace(UUID employeeId, String descriptor) {
        double[] parsed = parseDescriptor(descriptor);
        if (parsed == null) {
            throw new BadRequestException("Invalid or missing face descriptor.");
        }
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setFaceDescriptor(serializeDescriptor(parsed));
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void resetFace(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setFaceDescriptor(null);
        employeeRepository.save(employee);
    }

    // Resolves an employee code tolerantly: exact -> case-insensitive -> normalized "EMP001" => "EMP-001".
    private Employee findEmployeeByCodeFlexible(String raw) {
        String code = raw == null ? "" : raw.trim();
        return employeeRepository.findByEmployeeCode(code)
                .or(() -> employeeRepository.findByEmployeeCodeIgnoreCase(code))
                .or(() -> {
                    // Insert a dash between a leading letter group and trailing digits: EMP001 -> EMP-001
                    String normalized = code.toUpperCase().replaceAll("^([A-Z]+)[-_ ]?(\\d+)$", "$1-$2");
                    return employeeRepository.findByEmployeeCodeIgnoreCase(normalized);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found with code: " + raw + ". Use the exact code from the Employees list (e.g. EMP-001)."));
    }

    private Employee resolveEmployee(ManualAttendanceRequest request) {
        if (request.getEmployeeId() != null) {
            return employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        }
        if (request.getEmployeeCode() != null && !request.getEmployeeCode().isBlank()) {
            return employeeRepository.findByEmployeeCode(request.getEmployeeCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with code: " + request.getEmployeeCode()));
        }
        throw new BadRequestException("Either employee_id or employee_code is required");
    }

    // Accepts "HH:mm" (e.g. "08:30") or a full ISO date-time; returns null when blank
    private LocalDateTime parseTime(LocalDate date, String value) {
        if (value == null || value.isBlank()) return null;
        String v = value.trim();
        try {
            if (v.length() <= 5) { // "HH:mm"
                return date.atTime(java.time.LocalTime.parse(v));
            }
            return LocalDateTime.parse(v);
        } catch (Exception e) {
            throw new BadRequestException("Invalid time value: " + value + " (use HH:mm)");
        }
    }
}