package fitpet_be.application.serviceImpl;

import fitpet_be.application.service.ContactService;
import fitpet_be.domain.model.Contact;
import fitpet_be.domain.repository.ContactRepository;
import fitpet_be.domain.repository.EstimateRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final EstimateRepository estimateRepository;
    @Transactional
    public String addCount() {

        Contact contact = Contact.builder()
            .contactType("phone")
            .build();

        contactRepository.save(contact);
        return "연결중입니다";
    }

    @Override
    public Integer getCounts() {
        Integer todayPhoneCount = contactRepository.countContactsByCreatedAtToday(LocalDate.now());
        Integer todayTalksCount = estimateRepository.countEstimateByCreatedAtToday(LocalDate.now());

        return todayPhoneCount + todayTalksCount;
    }
}
