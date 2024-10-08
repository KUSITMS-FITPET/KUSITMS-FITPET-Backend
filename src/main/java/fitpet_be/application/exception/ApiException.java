package fitpet_be.application.exception;

import fitpet_be.application.dto.ErrorReasonDto;
import fitpet_be.common.ErrorStatus;

public class ApiException extends RuntimeException{

    private final ErrorStatus errorStatus;

    public ApiException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public ErrorReasonDto getErrorReason() {
        return this.errorStatus.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.errorStatus.getReasonHttpStatus();
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }
}
