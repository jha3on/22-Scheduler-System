package system.api.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import system.api.payload.query.TriggerLogQuery;
import system.api.payload.query.TriggerQuery;
import system.api.payload.request.TriggerSearchRequest;
import system.api.shape.SystemPageController;
import system.core.service.schedule.ScheduleQueryService;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize(value = "isAuthenticated()")
public class TriggerPageController implements SystemPageController {
    private final ScheduleQueryService scheduleQueryService;
    private final String TRIGGER_PAGE = "schedule/page/trigger";

    /**
     * 트리거 등록 화면
     */
    @GetMapping(value = {"/trigger/store"})
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public String triggerStore(@RequestParam String jobName,
                               @RequestParam String jobGroup, Model model) {
        model.addAttribute("jobName", jobName);
        model.addAttribute("jobGroup", jobGroup);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerStore");
    }

    /**
     * 트리거 상세 화면
     */
    @GetMapping(value = {"/trigger/{triggerSeq}"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerDetail(@PathVariable Long triggerSeq, Model model) {
        TriggerQuery trigger = scheduleQueryService.getTriggerSummary(triggerSeq);

        model.addAttribute("trigger", trigger);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerDetail");
    }

    /**
     * 트리거 목록 화면
     */
    @GetMapping(value = {"/trigger/list"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerList(@ModelAttribute TriggerSearchRequest request,
                              @PageableDefault Pageable pageable, Model model) {
        Page<TriggerQuery> results = scheduleQueryService.getTriggerSummaryList(request, pageable);

        model.addAttribute("url", "/trigger/list");
        model.addAttribute("sort", request.getSort()); // 정렬 방향
        model.addAttribute("sortBy", request.getSortBy()); // 정렬 조건
        model.addAttribute("keyword", request.getKeyword()); // 검색 단어
        model.addAttribute("results", results);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerListAll");
    }

    /**
     * 트리거 목록 화면
     */
    @GetMapping(value = {"/job/{jobSeq}/trigger/list"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerList(@PathVariable Long jobSeq,
                              @ModelAttribute TriggerSearchRequest request,
                              @PageableDefault Pageable pageable, Model model) {
        JobKey jobKey = scheduleQueryService.getJobKey(jobSeq);
        Page<TriggerQuery> results = scheduleQueryService.getTriggerSummaryList(jobSeq, request, pageable);

        model.addAttribute("url", String.format("/job/%d/trigger/list", jobSeq));
        model.addAttribute("sort", request.getSort()); // 정렬 방향
        model.addAttribute("sortBy", request.getSortBy()); // 정렬 조건
        model.addAttribute("keyword", request.getKeyword()); // 검색 단어
        model.addAttribute("jobName", jobKey.getName());
        model.addAttribute("jobGroup", jobKey.getGroup());
        model.addAttribute("results", results);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerList");
    }

    /**
     * 트리거 로그 상세 화면
     */
    @GetMapping(value = {"/trigger/log/{logSeq}"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerLogDetail(@PathVariable Long logSeq, Model model) {
        TriggerLogQuery log = scheduleQueryService.getTriggerLog(logSeq);

        model.addAttribute("log", log);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerLogDetail");
    }

    /**
     * 트리거 로그 목록 화면
     */
    @GetMapping(value = {"/trigger/log"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerLogList(@PageableDefault Pageable pageable, Model model) {
        Page<TriggerLogQuery> results = scheduleQueryService.getTriggerLogList(pageable);

        model.addAttribute("url", "/trigger/log");
        model.addAttribute("results", results);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerLogList");
    }

    /**
     * 트리거 로그 목록 화면
     */
    @GetMapping(value = {"/job/{jobSeq}/trigger/log"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerLogListByJob(@PathVariable Long jobSeq,
                                      @PageableDefault Pageable pageable, Model model) {
        Page<TriggerLogQuery> results = scheduleQueryService.getTriggerLogListByJob(jobSeq, pageable);

        model.addAttribute("url", String.format("/job/%d/trigger/log", jobSeq));
        model.addAttribute("results", results);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerLogList");
    }

    /**
     * 트리거 로그 목록 화면
     */
    @GetMapping(value = {"/trigger/{triggerSeq}/log"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public String triggerLogListByTrigger(@PathVariable Long triggerSeq,
                                          @PageableDefault Pageable pageable, Model model) {
        Page<TriggerLogQuery> results = scheduleQueryService.getTriggerLogListByTrigger(triggerSeq, pageable);

        model.addAttribute("url", String.format("/trigger/%d/log", triggerSeq));
        model.addAttribute("results", results);

        return String.format("%s/%s", TRIGGER_PAGE, "triggerLogList");
    }
}