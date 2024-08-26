package pitpet_be.pitpetbe.domain.estimate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pitpet_be.pitpetbe.domain.estimate.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate,Long> {

}
