package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.model.Attendance;
import hrd.com.hrms.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(Attendance attendance) {
        if (attendance == null) return null;

        Employee emp = attendance.getEmployee();
        String first = emp != null && emp.getFirstName() != null ? emp.getFirstName() : "";
        String last = emp != null && emp.getLastName() != null ? emp.getLastName() : "";
        String fullName = (first + " " + last).trim();
        if (fullName.isEmpty()) fullName = "Unknown Employee";

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
                .status(attendance.getStatus())
                .scanType(attendance.getScanType())
                .selfieUrl(attendance.getSelfieUrl())
                .build();
    }
}