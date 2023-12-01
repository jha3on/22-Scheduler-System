package system.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import system.api.payload.request.UserSignRequest;
import system.api.shape.SystemRestController;
import system.core.service.UserValidator;
import system.core.enums.UserGradeType;
import system.core.service.UserQueryService;
import system.core.service.UserSignService;
import system.share.base.response.Result;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
@PreAuthorize(value = "permitAll()")
public class UserRestController implements SystemRestController {
    private final UserValidator userValidator;
    private final UserSignService userSignService;
    private final UserQueryService userQueryService;

    /**
     * 사용자 중복을 확인한다.
     * <p> : 사용자 사번의 중복을 확인한다.
     */
    @GetMapping(value = {"/users/code"})
    public ResponseEntity<?> isDuplicateUserCode(@RequestParam String userCode) {
        return Result.ofSuccess(userQueryService.existsUserCode(userCode));
    }

    /**
     * 사용자 중복을 확인한다.
     * <p> : 사용자 이메일의 중복을 확인한다.
     */
    @GetMapping(value = {"/users/email"})
    public ResponseEntity<?> isDuplicateUserEmail(@RequestParam String userEmail) {
        return Result.ofSuccess(userQueryService.existsUserEmail(userEmail));
    }

    /**
     * 사용자 정보를 등록한다.
     */
    @PostMapping(value = {"/users/sign"})
    public ResponseEntity<?> userSign(@RequestBody @Valid UserSignRequest request) {
        return Result.ofSuccess(userSignService.sign(request, UserGradeType.SCHED));
    }

    @InitBinder(value = {"userSignRequest"})
    protected void setUserValidator(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }
}