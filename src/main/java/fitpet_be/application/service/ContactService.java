package fitpet_be.application.service;

import fitpet_be.application.dto.response.ContactCountResponse;
import fitpet_be.application.serviceImpl.ContactServiceImpl;

public interface ContactService {

    String addCount();

    ContactCountResponse getCounts();

}
