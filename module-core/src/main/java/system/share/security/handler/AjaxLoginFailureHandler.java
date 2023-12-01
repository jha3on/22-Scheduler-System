package system.share.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import system.core.exception.UserLoginException;
import system.share.base.response.ResultType;
import system.share.base.response.ResultSender;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AjaxLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException e
    ) throws IOException {
        error(e, response);
    }

    private void error(AuthenticationException e, HttpServletResponse response) throws IOException {
        if (e instanceof UserLoginException) {
            ResultSender.set(((UserLoginException) e).getType(), HttpStatus.UNAUTHORIZED, response);
        } else {
            ResultSender.set(ResultType.FAILURE, HttpStatus.INTERNAL_SERVER_ERROR, response);
        }
    }
}