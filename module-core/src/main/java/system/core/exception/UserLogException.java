package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.common.exception.CommonException;
import system.share.base.response.ResultType;

@Getter
public class UserLogException extends CommonException {
    protected UserLogException(ResultType type, HttpStatus status) {
        super(type, status);
    }

    public static UserLogException userLogNotFound() {
        return new UserLogException(ResultType.USER_LOG_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static UserLogException userLogTypeNotFound() {
        return new UserLogException(ResultType.USER_LOG_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}