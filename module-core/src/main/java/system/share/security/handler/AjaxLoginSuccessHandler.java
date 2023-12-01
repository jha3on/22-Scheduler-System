package system.share.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import system.share.base.response.ResultType;
import system.share.base.response.ResultSender;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class AjaxLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException {
        clearSession(request);
        setRedirectUrl(request, response);
    }

    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (Objects.nonNull(session)) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    private void setRedirectUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (Objects.nonNull(savedRequest)) {
            useSessionUrl(request, response);
        } else {
            useDefaultUrl(response);
        }
    }

    private void useSessionUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String target = requestCache.getRequest(request, response).getRedirectUrl();

        ResultSender.set(ResultType.SUCCESS, target, HttpStatus.OK, response);
    }

    private void useDefaultUrl(HttpServletResponse response) throws IOException {
        String target = "/";
        
        ResultSender.set(ResultType.SUCCESS, target, HttpStatus.OK, response);
    }
}