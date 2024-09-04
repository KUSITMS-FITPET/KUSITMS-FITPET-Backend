package fitpet_be.domain.repository;

import fitpet_be.infrastructure.persistence.JpaContactRepository;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepository extends JpaContactRepository {
    @Query("SELECT COUNT(c) FROM Contact c WHERE DATE(c.createdAt) = :today")
    Integer countContactsByCreatedAtToday(@Param("today") LocalDate today);
}
