package fitpet_be.domain.repository;

import fitpet_be.domain.model.Faq;
import fitpet_be.infrastructure.persistence.JpaFaqRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface FaqRepository extends JpaFaqRepository {

}
