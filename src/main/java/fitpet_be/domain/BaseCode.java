package fitpet_be.domain;

import fitpet_be.application.dto.ReasonDto;

public interface BaseCode {

    public ReasonDto getReason();

    public ReasonDto getReasonHttpStatus();

}
