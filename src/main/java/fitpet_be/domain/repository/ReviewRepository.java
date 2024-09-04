package fitpet_be.domain.repository;

import fitpet_be.domain.model.Review;
import fitpet_be.infrastructure.persistence.JpaReviewRepository;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaReviewRepository {

    Page<Review> findAll(Pageable pageable);

    Page<Review> findAllByPetInfo(@Param("petInfo") String petInfo, Pageable pageable);


    @Query("SELECT count(r) FROM Review r")
    Long reviewsTotalCount();

    Optional<Review> findById(@Param("reviewId") Long reviewId);

}
