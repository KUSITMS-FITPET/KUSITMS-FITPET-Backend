package fitpet_be.domain.repository;

import fitpet_be.infrastructure.persistence.JpaEstimateRepository;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

public interface EstimateRepository extends JpaEstimateRepository {

    @Query("SELECT COUNT(e) FROM Estimate e WHERE DATE(e.createdAt) = :today")
    Long countEstimateByCreatedAtToday(@Param("today") LocalDate today);

}
