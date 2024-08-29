package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFaqRepository extends JpaRepository<Faq, Long> {
}
