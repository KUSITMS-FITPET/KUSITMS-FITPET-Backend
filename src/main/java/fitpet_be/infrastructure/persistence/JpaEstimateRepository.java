package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Estimate;
import fitpet_be.domain.repository.EstimateRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEstimateRepository extends JpaRepository<Estimate, Long>, EstimateRepository {
}
