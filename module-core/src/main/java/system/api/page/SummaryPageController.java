package system.api.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import system.api.payload.query.JobLogQuery;
import system.api.payload.query.UserLogQuery;
import system.api.shape.SystemPageController;
import system.core.service.UserQueryService;
import system.core.service.schedule.ScheduleQueryService;
import java.util.List;
import static system.core.enums.schedule.ScheduleStateType.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
public class SummaryPageController implements SystemPageController {
    private final UserQueryService userQueryService;
    private final ScheduleQueryService scheduleQueryService;

    /**
     * 스케줄, 스케줄 로그 요약 화면
     */
    @GetMapping(value = {"/", "/summary"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String summary(Model model) {
        Long jobCount = scheduleQueryService.getJobSummaryCount();
        Long jobCountByRunning = scheduleQueryService.getJobSummaryCount(EXECUTED, PROCESSING);
        Long jobCountByComplete = scheduleQueryService.getJobSummaryCount(COMPLETED);

        Long jobLogCount = scheduleQueryService.getJobLogCount();
        Long userLogCount = userQueryService.getUserLogCount();
        List<JobLogQuery> jobLogList = scheduleQueryService.getJobLogList();
        List<UserLogQuery> userLogList = userQueryService.getUserLogList();

        model.addAttribute("jobCount", jobCount);
        model.addAttribute("jobCountByRunning", jobCountByRunning);
        model.addAttribute("jobCountByComplete", jobCountByComplete);

        model.addAttribute("jobLogList", jobLogList);
        model.addAttribute("jobLogCount", jobLogCount);
        model.addAttribute("userLogList", userLogList);
        model.addAttribute("userLogCount", userLogCount);

        return String.format("%s/%s", "schedule/page/", "summary");
    }
}