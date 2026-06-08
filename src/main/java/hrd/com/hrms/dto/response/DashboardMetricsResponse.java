package hrd.com.hrms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardMetricsResponse {
    private long totalWorkforce;
    private long activeStaff;
    private long pendingLeaves;
    private long presentToday;

    // Percent trends vs last month
    private int totalWorkforceTrend;
    private int activeStaffTrend;
    private int pendingLeavesTrend;
    private int presentTodayTrend;
}