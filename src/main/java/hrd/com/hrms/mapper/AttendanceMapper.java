package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.attendance.AttendanceRequest;
import hrd.com.hrms.dto.attendance.AttendanceResponse;
import hrd.com.hrms.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public Attendance toEntity(AttendanceRequest request) {
        if (request == null) return null;

        Attendance attendance = new Attendance();
        attendance.setClockIn(request.getClockIn());
        attendance.setClockOut(request.getClockOut());
        attendance.setStatus(request.getStatus());
        return attendance;
    }

    public AttendanceResponse toResponse(Attendance attendance) {
        if (attendance == null) return null;

        String fullName = "";
        if (attendance.getEmployee() != null) {
            fullName = attendance.getEmployee().getFirstName() + " " + attendance.getEmployee().getLastName();
        }

        return new AttendanceResponse(
                attendance.getId(),
                attendance.getEmployee() != null ? attendance.getEmployee().getId() : null,
                fullName,
                attendance.getClockIn(),
                attendance.getClockOut(),
                attendance.getStatus()
        );
    }
}