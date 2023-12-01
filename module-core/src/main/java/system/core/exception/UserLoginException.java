package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import system.share.base.response.ResultType;

@Getter
public class UserLoginException extends AuthenticationException {
    private final ResultType type;
    private final HttpStatus status;

    protected UserLoginException(ResultType type, HttpStatus status) {
        super(type.getMessage());
        this.type = type;
        this.status = status;
    }

    public static UserLoginException userNotFound() {
        return new UserLoginException(ResultType.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
    }

    public static UserLoginException userLoginMismatch() {
        return new UserLoginException(ResultType.USER_LOGIN_MISMATCH, HttpStatus.UNAUTHORIZED);
    }
}