package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Test;
import fitpet_be.domain.repository.TestRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTestRepository extends JpaRepository<Test, Long> {
}
