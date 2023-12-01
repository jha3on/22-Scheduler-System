package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.common.exception.CommonException;
import system.share.base.response.ResultType;

@Getter
public class UserException extends CommonException {
    protected UserException(ResultType type, HttpStatus status) {
        super(type, status);
    }

    public static UserException userNotFound() {
        return new UserException(ResultType.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static UserException userRoleNotFound() {
        return new UserException(ResultType.USER_ROLE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static UserException userGradeNotFound() {
        return new UserException(ResultType.USER_GRADE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static UserException userSortTypeNotFound() {
        return new UserException(ResultType.USER_SORT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static UserException authenticationNotFound() {
        return new UserException(ResultType.AUTHENTICATION_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static UserException duplicateUserCode() {
        return new UserException(ResultType.DUPLICATE_USER_CODE, HttpStatus.CONFLICT);
    }

    public static UserException duplicateUserEmail() {
        return new UserException(ResultType.DUPLICATE_USER_EMAIL, HttpStatus.CONFLICT);
    }
}