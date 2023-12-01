package system.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import system.api.payload.request.*;
import system.api.payload.response.TriggerPathResponse;
import system.api.shape.SystemRestController;
import system.core.service.schedule.ScheduleService;
import system.core.service.schedule.trigger.TriggerValidator;
import system.share.base.response.Result;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@PreAuthorize(value = "isAuthenticated()")
public class TriggerRestController implements SystemRestController {
    private final TriggerValidator triggerValidator;
    private final ScheduleService scheduleService;

    /**
     * 트리거 스케줄을 등록한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @PostMapping(value = "/triggers")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> schedule(@RequestBody @Valid TriggerStoreRequest triggerRequest) {
        TriggerPathResponse result = scheduleService.schedule(triggerRequest);

        return Result.ofSuccess(result);
    }

    /**
     * 트리거 스케줄을 수정한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @PutMapping(value = "/triggers")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> update(@RequestBody @Valid TriggerUpdateRequest triggerRequest) {
        System.out.println(triggerRequest);
        TriggerPathResponse result = scheduleService.reSchedule(triggerRequest);

        return Result.ofSuccess(result);
    }

    /**
     * 트리거 스케줄 상태를 수정한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @PutMapping(value = "/triggers/state")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> state(@RequestBody @Valid TriggerStateRequest triggerRequest) {
        TriggerPathResponse result = scheduleService.switchState(triggerRequest);

        return Result.ofSuccess(result);
    }

    /**
     * 트리거 스케줄을 삭제한다.
     * <p> : 스케줄 관리, 실행 권한을 가진 사용자를 확인한다.
     */
    @DeleteMapping(value = "/triggers")
    @PreAuthorize(value = "@userUtils.hasRoles({'W', 'X'})")
    public ResponseEntity<?> delete(@RequestBody @Valid TriggerDeleteRequest triggerRequest) {
        scheduleService.unSchedule(triggerRequest);

        return Result.ofSuccess();
    }

    /**
     * 트리거 등록, 수정에 관한 요청 값을 추가 검증한다.
     */
    @InitBinder(value = {"triggerStoreRequest", "triggerUpdateRequest"})
    protected void setTriggerValidator(WebDataBinder binder) {
        binder.addValidators(triggerValidator);
    }
}