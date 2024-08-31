package fitpet_be.application.serviceImpl;

import fitpet_be.application.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimateServiceImpl{

}
