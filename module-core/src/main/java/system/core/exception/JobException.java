package system.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import system.share.base.common.exception.CommonException;
import system.share.base.response.ResultType;

@Getter
public class JobException extends CommonException {
    protected JobException(ResultType type, HttpStatus status) {
        super(type, status);
    }

    public static JobException jobEmpty() {
        return new JobException(ResultType.JOB_EMPTY, HttpStatus.NOT_FOUND);
    }

    public static JobException jobNotFound() {
        return new JobException(ResultType.JOB_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static JobException jobClassNotFound() {
        return new JobException(ResultType.JOB_CLASS_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static JobException jobClassTypeNotFound() {
        return new JobException(ResultType.JOB_CLASS_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static JobException jobSortTypeNotFound() {
        return new JobException(ResultType.JOB_SORT_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static JobException jobCreation() {
        return new JobException(ResultType.JOB_CREATION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static JobException jobUpdation() {
        return new JobException(ResultType.JOB_UPDATION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static JobException jobDeletion() {
        return new JobException(ResultType.JOB_DELETION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static JobException jobSwitch() {
        return new JobException(ResultType.JOB_SWITCH, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static JobException jobDuplication() {
        return new JobException(ResultType.JOB_DUPLICATION, HttpStatus.CONFLICT);
    }

    public static JobException jobSerialization() {
        return new JobException(ResultType.JOB_SERIALIZATION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static JobException jobDeserialization() {
        return new JobException(ResultType.JOB_DESERIALIZATION, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static JobException jobRegisterMismatch() {
        return new JobException(ResultType.JOB_REGISTER_MISMATCH, HttpStatus.BAD_REQUEST);
    }

    public static JobException jobClassTypeMismatch() {
        return new JobException(ResultType.JOB_CLASS_TYPE_MISMATCH, HttpStatus.BAD_REQUEST);
    }
}