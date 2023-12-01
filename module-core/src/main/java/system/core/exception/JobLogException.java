package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.common.exception.CommonException;
import system.share.base.response.ResultType;

@Getter
public class JobLogException extends CommonException {
    protected JobLogException(ResultType type, HttpStatus status) {
        super(type, status);
    }

    public static JobLogException jobLogNotFound() {
        return new JobLogException(ResultType.JOB_LOG_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static JobLogException jobLogDuplication() {
        return new JobLogException(ResultType.JOB_LOG_DUPLICATION, HttpStatus.CONFLICT);
    }
}