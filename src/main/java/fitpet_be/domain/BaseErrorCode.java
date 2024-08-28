package fitpet_be.domain;

import fitpet_be.application.dto.ErrorReasonDto;

public interface BaseErrorCode {

    public ErrorReasonDto getReason();

    public ErrorReasonDto getReasonHttpStatus();

}
