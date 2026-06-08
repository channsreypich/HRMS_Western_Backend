package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.AttendanceResponse;
import hrd.com.hrms.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceResponse toResponse(Attendance attendance) {
        if (attendance == null) return null;

        String fullName = attendance.getEmployee() != null ?
                attendance.getEmployee().getUser().getUsername() : "Unknown Employee";

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(fullName)
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .status(attendance.getStatus())
                .scanType(attendance.getScanType())
                .build();
    }
}