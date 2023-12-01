package system.share.base.resolver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import system.api.shape.SystemPageController;
import system.share.base.response.ResultSender;
import system.share.base.response.ResultType;
import system.share.security.SecurityUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static system.share.base.response.ResultType.UNAUTHENTICATED_REQUEST;
import static system.share.base.response.ResultType.UNAUTHORIZED_REQUEST;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice(assignableTypes = {SystemPageController.class})
public class GlobalPageExceptionResolver {

    /**
     * 인증, 인가 예외를 처리한다.
     */
    @ExceptionHandler(value = {AuthenticationException.class, AccessDeniedException.class})
    public void handleSecurity(HttpServletRequest request, HttpServletResponse response, RuntimeException e) throws ServletException, IOException {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        ofSecurity(request, response, e);
    }

    /**
     * 서버에서 발생한 예외를 처리한다.
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public void handleDefault(HttpServletRequest request, HttpServletResponse response, RuntimeException e) throws ServletException, IOException {
        log.info("[INFO] Exception: {} :: [MORE] Message: {}",
                e.getClass().getCanonicalName(),
                e.getMessage());

        ofDefault(request, response);
    }

    private void ofSecurity(HttpServletRequest request, HttpServletResponse response, RuntimeException e) throws ServletException, IOException {
        String errorPage = "/error/security";

        if (e instanceof AccessDeniedException) {
            if (!SecurityUtils.isAuthenticated()) {
                ResultSender.set(UNAUTHENTICATED_REQUEST, UNAUTHORIZED, errorPage, "/user/login", request, response);
                SecurityUtils.clearAuthentication();
            } else ResultSender.set(UNAUTHORIZED_REQUEST, FORBIDDEN, errorPage, refererPage(request), request, response);
        }

        if (e instanceof AuthenticationException) {
            if (!SecurityUtils.isAuthenticated()) {
                ResultSender.set(UNAUTHENTICATED_REQUEST, UNAUTHORIZED, errorPage, "/user/login", request, response);
                SecurityUtils.clearAuthentication();
            } else ResultSender.set(UNAUTHORIZED_REQUEST, FORBIDDEN, errorPage, refererPage(request), request, response);
        }
    }

    private void ofDefault(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultSender.set(ResultType.FAILURE, HttpStatus.BAD_REQUEST, "/error", refererPage(request), request, response);
    }

    private String refererPage(HttpServletRequest request) {
        String referer = request.getHeader("referer");

        return StringUtils.isEmpty(referer) ? "/" : referer;
    }
}