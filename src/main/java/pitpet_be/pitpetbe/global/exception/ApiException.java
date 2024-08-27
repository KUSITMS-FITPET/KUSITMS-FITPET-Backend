package pitpet_be.pitpetbe.global.exception;

import pitpet_be.pitpetbe.global.response.code.ErrorReasonDto;
import pitpet_be.pitpetbe.global.response.code.status.ErrorStatus;

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
