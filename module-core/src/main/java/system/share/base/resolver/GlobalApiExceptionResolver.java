package system.share.base.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import system.api.shape.SystemRestController;
import system.share.base.response.Result;
import system.share.base.response.ResultType;
import system.share.base.response.ValidationResult;
import system.share.base.common.exception.CommonException;

@Slf4j
@RestControllerAdvice(assignableTypes = {SystemRestController.class})
public class GlobalApiExceptionResolver {

    /**
     * 요청 값의 유효성과 관련된 예외를 처리한다.
     * <p> : @ModelAttribute, @Valid 어노테이션 기준
     */
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity<?> handleValidation(BindException e) {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        return Result.of(HttpStatus.BAD_REQUEST, ResultType.INVALID_VALUE, ValidationResult.create(e));
    }

    /**
     * 요청 값의 유효성과 관련된 예외를 처리한다.
     * <p> : @RequestBody, @Valid 어노테이션 기준
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        return Result.of(HttpStatus.BAD_REQUEST, ResultType.INVALID_VALUE, ValidationResult.create(e));
    }

    /**
     * 요청 값의 유효성과 관련된 예외를 처리한다.
     * <p> : Enum 값 기준
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleValidation(MethodArgumentTypeMismatchException e) {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        return Result.of(HttpStatus.BAD_REQUEST, ResultType.INVALID_VALUE, ValidationResult.create(e));
    }

    /**
     * JSON 값의 유효성과 관련된 예외를 처리한다.
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    private ResponseEntity<?> handleValidation(HttpMessageNotReadableException e) {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        Throwable cause = e.getCause(); // JsonMappingException, ...
        return Result.of(HttpStatus.BAD_REQUEST, ResultType.INVALID_JSON);
    }

    /**
     * 인증, 인가 예외를 처리한다.
     */
    @ExceptionHandler(value = {AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<?> handleSecurity(RuntimeException e) {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        return (e instanceof AuthenticationException)
                ? Result.of(HttpStatus.UNAUTHORIZED, ResultType.UNAUTHENTICATED_REQUEST)
                : Result.of(HttpStatus.FORBIDDEN, ResultType.UNAUTHORIZED_REQUEST);
    }

    /**
     * 서버에서 발생한 예외를 처리한다.
     */
    @ExceptionHandler(value = {CommonException.class, RuntimeException.class})
    public ResponseEntity<?> handleDefault(RuntimeException e) {
        e.printStackTrace();

        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        return (e instanceof CommonException)
                ? Result.of(HttpStatus.INTERNAL_SERVER_ERROR, ((CommonException) e).getType(), e.getMessage())
                : Result.of(HttpStatus.INTERNAL_SERVER_ERROR, ResultType.FAILURE);
    }
}