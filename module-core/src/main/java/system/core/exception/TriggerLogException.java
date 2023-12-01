package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.common.exception.CommonException;
import system.share.base.response.ResultType;

@Getter
public class TriggerLogException extends CommonException {
    protected TriggerLogException(ResultType type, HttpStatus status) {
        super(type, status);
    }

    public static TriggerLogException triggerLogNotFound() {
        return new TriggerLogException(ResultType.TRIGGER_LOG_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static TriggerLogException triggerLogDuplication() {
        return new TriggerLogException(ResultType.TRIGGER_LOG_DUPLICATION, HttpStatus.CONFLICT);
    }
}