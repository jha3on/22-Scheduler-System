package system.share.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import system.share.security.SecurityUtils;
import system.share.base.response.ResultSender;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static system.share.base.response.ResultType.UNAUTHENTICATED_REQUEST;
import static system.share.base.response.ResultType.UNAUTHORIZED_REQUEST;

@Slf4j
public class UnauthorizedRequestHandler implements AccessDeniedHandler {

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException e
    ) throws IOException, ServletException {
        String target = request.getRequestURI(); // or request.getContextPath(), target.contains("api");

        if (target.startsWith("/api")) ofRest(response);
        else ofPage(request, response);
    }

    private void ofRest(HttpServletResponse response) throws IOException {
        if (!SecurityUtils.isAuthenticated()) {
            ResultSender.set(UNAUTHENTICATED_REQUEST, UNAUTHORIZED, response);
        } else ResultSender.set(UNAUTHORIZED_REQUEST, FORBIDDEN, response);
    }

    private void ofPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorPage = "/error/security";

        if (!SecurityUtils.isAuthenticated()) {
            ResultSender.set(UNAUTHENTICATED_REQUEST, UNAUTHORIZED, errorPage, "/user/login", request, response);
            SecurityUtils.clearAuthentication();
        } else ResultSender.set(UNAUTHORIZED_REQUEST, FORBIDDEN, errorPage, refererPage(request), request, response);
    }

    private String refererPage(HttpServletRequest request) {
        String referer = request.getHeader("referer");

        return StringUtils.isEmpty(referer) ? "/" : referer;
    }
}