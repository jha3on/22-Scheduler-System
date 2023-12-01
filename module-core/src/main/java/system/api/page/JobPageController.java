package system.api.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import system.api.payload.query.JobLogQuery;
import system.api.payload.query.JobQuery;
import system.api.payload.request.JobSearchRequest;
import system.api.shape.SystemPageController;
import system.core.service.schedule.ScheduleQueryService;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
public class JobPageController implements SystemPageController {
    private final ScheduleQueryService scheduleQueryService;
    private final String JOB_PAGE = "schedule/page/job";

    /**
     * 작업 등록 화면
     */
    @GetMapping(value = {"/job/store"})
    @PreAuthorize(value = "@userUtils.hasRoles({'W'})")
    public String jobStore() {
        return String.format("%s/%s", JOB_PAGE, "jobStore");
    }

    /**
     * 작업 상세 화면
     */
    @GetMapping(value = {"/job/{jobSeq}"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String jobDetail(@PathVariable Long jobSeq, Model model) {
        JobQuery job = scheduleQueryService.getJobSummary(jobSeq);

        model.addAttribute("job", job);

        return String.format("%s/%s", JOB_PAGE, "jobDetail");
    }

    /**
     * 작업 목록 화면
     */
    @GetMapping(value = {"/job/list"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String jobList(@ModelAttribute JobSearchRequest request,
                          @PageableDefault Pageable pageable, Model model) {
        Page<JobQuery> results = scheduleQueryService.getJobSummaryList(request, pageable);

        model.addAttribute("url", "/job/list");
        model.addAttribute("sort", request.getSort()); // 정렬 방향
        model.addAttribute("sortBy", request.getSortBy()); // 정렬 조건
        model.addAttribute("keyword", request.getKeyword()); // 검색 단어
        model.addAttribute("results", results);

        return String.format("%s/%s", JOB_PAGE, "jobList");
    }

    /**
     * 작업 로그 상세 화면
     */
    @GetMapping(value = {"/job/log/{logSeq}"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String jobLogDetail(@PathVariable Long logSeq, Model model) {
        JobLogQuery log = scheduleQueryService.getJobLog(logSeq);

        model.addAttribute("log", log);

        return String.format("%s/%s", JOB_PAGE, "jobLogDetail");
    }

    /**
     * 작업 로그 목록 화면
     */
    @GetMapping(value = {"/job/log"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String jobLogList(@PageableDefault Pageable pageable, Model model) {
        Page<JobLogQuery> results = scheduleQueryService.getJobLogList(pageable);

        model.addAttribute("url", "/job/log");
        model.addAttribute("results", results);

        return String.format("%s/%s", JOB_PAGE, "jobLogList");
    }

    /**
     * 작업 로그 목록 화면
     */
    @GetMapping(value = {"/job/{jobSeq}/log"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String jobLogList(@PathVariable Long jobSeq,
                             @PageableDefault Pageable pageable, Model model) {
        Page<JobLogQuery> results = scheduleQueryService.getJobLogList(jobSeq, pageable);

        model.addAttribute("url", String.format("/job/%d/log", jobSeq));
        model.addAttribute("results", results);

        return String.format("%s/%s", JOB_PAGE, "jobLogList");
    }
}