package fitpet_be.application.serviceImpl;

import fitpet_be.application.request.TestServiceRequest;
import fitpet_be.application.response.TestResponse;
import fitpet_be.application.service.TestService;
import fitpet_be.domain.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;

    @Override
    public TestResponse getTestResponse(TestServiceRequest request) {

        testRepository.save(request.toEntity());

        return TestResponse.builder()
                .message("Test 저장 성공")
                .build();

    }

}
