package fitpet_be.domain.repository;

import fitpet_be.domain.model.Faq;
import fitpet_be.infrastructure.persistence.JpaFaqRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface FaqRepository extends JpaFaqRepository {

    @Query("SELECT f FROM Faq f WHERE f.category = ?1 ")
    Page<Faq> findAllByCategory(Long category, Pageable pageable);

    @Query("SELECT COUNT(f) FROM Faq f WHERE f.category = :category")
    Long faqTotalCountByCategory(@Param("category") Long category);

    @Query("SELECT f FROM Faq f WHERE f.category = :category AND f.question LIKE %:keyword%")
    Page<Faq> searchAllByKeywordAndCategory(@Param("keyword") String keyword, @Param("category") Long category, Pageable pageable);

}
