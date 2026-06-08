package hrd.com.hrms.mapper;

import hrd.com.hrms.dto.response.LeaveResponse;
import hrd.com.hrms.model.Leave;
import org.springframework.stereotype.Component;

@Component
public class LeaveMapper {
    public LeaveResponse toResponse(Leave leave) {
        if (leave == null) return null;

        String fullName = (leave.getEmployee() != null)
                ? leave.getEmployee().getFirstName() + " " + leave.getEmployee().getLastName()
                : "Unknown Employee";

        return LeaveResponse.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployee().getId())
                .employeeName(fullName)
                .employeeCode(leave.getEmployee().getEmployeeCode())
                .leaveType(leave.getLeaveType())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .documentPath(leave.getDocumentPath())
                .build();
    }
}