package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
}
