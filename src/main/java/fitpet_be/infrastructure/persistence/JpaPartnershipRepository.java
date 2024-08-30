package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPartnershipRepository extends JpaRepository<Partnership, Long> {
}
