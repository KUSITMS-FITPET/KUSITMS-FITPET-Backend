package fitpet_be.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContactCountResponse {

    private Long phoneContacts;
    private Long TalksContacts;

    @Builder
    public ContactCountResponse(
        Long phoneContacts, Long TalksContacts) {

        this.phoneContacts = phoneContacts;
        this.TalksContacts = TalksContacts;

    }

}
