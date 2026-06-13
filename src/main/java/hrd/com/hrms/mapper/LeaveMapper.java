package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.model.Leave;
import org.springframework.stereotype.Component;

@Component
public class LeaveMapper {
    public LeaveResponse toResponse(Leave leave) {
        if (leave == null) return null;

        var emp = leave.getEmployee();
        String first = emp != null && emp.getFirstName() != null ? emp.getFirstName() : "";
        String last = emp != null && emp.getLastName() != null ? emp.getLastName() : "";
        String fullName = (first + " " + last).trim();
        if (fullName.isEmpty()) fullName = "Unknown Employee";

        long duration = java.time.temporal.ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;

        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(emp != null ? emp.getId() : null)
                .employeeName(fullName)
                .firstName(emp != null ? emp.getFirstName() : null)
                .lastName(emp != null ? emp.getLastName() : null)
                .employeeCode(emp != null ? emp.getEmployeeCode() : null)
                .departmentName(emp != null && emp.getDepartment() != null ? emp.getDepartment().getName() : null)
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .durationDays(duration)
                .reason(leave.getReason())
                .status(leave.getStatus())
                .documentPath(leave.getDocumentPath())
                .build();
    }
}