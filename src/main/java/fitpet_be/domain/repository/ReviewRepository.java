package fitpet_be.domain.repository;

import fitpet_be.domain.model.Review;
import fitpet_be.infrastructure.persistence.JpaReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaReviewRepository {

    @Query("SELECT r FROM Review r ORDER BY r.createdAt DESC ")
    Page<Review> findAllByOrderByDesc(Pageable pageable);


    @Query("SELECT r FROM Review r ORDER BY r.createdAt ASC ")
    Page<Review> findAllByOrderByAsc(Pageable pageable);

    @Query("SELECT count(r) FROM Review r")
    Long reviewsTotalCount();
}
