package system.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import system.api.payload.request.JobDeleteRequest;
import system.api.payload.request.JobStateRequest;
import system.api.payload.request.JobStoreRequest;
import system.api.payload.request.JobUpdateRequest;
import system.api.payload.response.JobPathResponse;
import system.api.shape.SystemRestController;
import system.core.service.schedule.ScheduleQueryService;
import system.core.service.schedule.ScheduleService;
import system.core.service.schedule.job.JobValidator;
import system.share.base.response.Result;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@PreAuthorize(value = "isAuthenticated()")
public class JobRestController implements SystemRestController {
    private final JobValidator jobValidator;
    private final ScheduleService scheduleService;
    private final ScheduleQueryService scheduleQueryService;

    /**
     * 작업 중복을 확인한다.
     * <p> : 작업 이름, 작업 그룹의 중복을 확인한다.
     */
    @GetMapping(value = {"/jobs/names"})
    @PreAuthorize(value = "@userUtils.hasRoles({'R'})")
    public ResponseEntity<?> isDuplicateUserCode(@RequestParam String jobName,
                                                 @RequestParam String jobGroup) {
        return Result.ofSuccess(scheduleQueryService.existsJobSummary(jobName, jobGroup));
    }

    /**
     * 작업 스케줄을 등록한다.
     * <p> : 스케줄 관리 권한을 가진 사용자를 확인한다.
     */
    @PostMapping(value = "/jobs")
    @PreAuthorize(value = "@userUtils.hasRoles({'W'})")
    public ResponseEntity<?> schedule(@RequestBody @Valid JobStoreRequest jobRequest) {
        JobPathResponse result = scheduleService.schedule(jobRequest);

        return Result.ofSuccess(result);
    }

    /**
     * 작업 스케줄을 수정한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @PutMapping(value = "/jobs")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> update(@RequestBody @Valid JobUpdateRequest jobRequest) {
        JobPathResponse result = scheduleService.reSchedule(jobRequest);

        return Result.ofSuccess(result);
    }

    /**
     * 작업 스케줄 상태를 수정한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @PutMapping(value = "/jobs/state")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> state(@RequestBody @Valid JobStateRequest jobRequest) {
        JobPathResponse result = scheduleService.switchState(jobRequest);

        return Result.ofSuccess(result);
    }

    /**
     * 작업 스케줄을 삭제한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @DeleteMapping(value = "/jobs")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> delete(@RequestBody @Valid JobDeleteRequest jobRequest) {
        scheduleService.unSchedule(jobRequest);

        return Result.ofSuccess();
    }

    /**
     * 작업 등록, 수정에 관한 요청 값을 추가 검증한다.
     */
    @InitBinder(value = {"jobStoreRequest", "jobUpdateRequest"})
    protected void setJobValidator(WebDataBinder binder) {
        binder.addValidators(jobValidator);
    }
}