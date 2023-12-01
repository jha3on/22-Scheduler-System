package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.common.exception.CommonException;
import system.share.base.response.ResultType;

@Getter
public class TriggerException extends CommonException {
    protected TriggerException(ResultType type, HttpStatus status) {
        super(type, status);
    }

    public static TriggerException triggerEmpty() {
        return new TriggerException(ResultType.TRIGGER_EMPTY, HttpStatus.NOT_FOUND);
    }

    public static TriggerException triggerNotFound() {
        return new TriggerException(ResultType.TRIGGER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static TriggerException triggerSortTypeNotFound() {
        return new TriggerException(ResultType.TRIGGER_SORT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static TriggerException triggerCreation() {
        return new TriggerException(ResultType.TRIGGER_CREATION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static TriggerException triggerUpdation() {
        return new TriggerException(ResultType.TRIGGER_UPDATION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static TriggerException triggerDeletion() {
        return new TriggerException(ResultType.TRIGGER_DELETION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static TriggerException triggerSwitch() {
        return new TriggerException(ResultType.TRIGGER_SWITCH, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static TriggerException triggerDuplication() {
        return new TriggerException(ResultType.TRIGGER_DUPLICATION, HttpStatus.CONFLICT);
    }

    public static TriggerException invalidTriggerTimeValue() {
        return new TriggerException(ResultType.INVALID_TRIGGER_TIME_VALUE, HttpStatus.BAD_REQUEST);
    }

    public static TriggerException invalidTriggerRepeatValue() {
        return new TriggerException(ResultType.INVALID_TRIGGER_REPEAT_VALUE, HttpStatus.BAD_REQUEST);
    }

    public static TriggerException triggerRegisterMismatch() {
        return new TriggerException(ResultType.TRIGGER_REGISTER_MISMATCH, HttpStatus.BAD_REQUEST);
    }

    public static TriggerException triggerClassTypeMismatch() {
        return new TriggerException(ResultType.TRIGGER_CLASS_TYPE_MISMATCH, HttpStatus.BAD_REQUEST);
    }

    public static TriggerException unsupportedTriggerClassType() {
        return new TriggerException(ResultType.UNSUPPORTED_TRIGGER_CLASS_TYPE, HttpStatus.BAD_REQUEST);
    }

    public static TriggerException unsupportedTriggerPolicyType() {
        return new TriggerException(ResultType.UNSUPPORTED_TRIGGER_POLICY_TYPE, HttpStatus.BAD_REQUEST);
    }
}