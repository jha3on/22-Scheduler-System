package system.share.base.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.response.ResultType;

@Getter
public class CommonException extends RuntimeException {
    private final ResultType type;
    private final HttpStatus status;

    protected CommonException(ResultType type, HttpStatus status) {
        super(type.getMessage());
        this.type = type;
        this.status = status;
    }

    public static CommonException sortTypeNotFound() {
        return new CommonException(ResultType.SORT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}