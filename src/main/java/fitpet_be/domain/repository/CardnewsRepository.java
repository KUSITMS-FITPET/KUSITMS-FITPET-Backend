package fitpet_be.domain.repository;

import fitpet_be.domain.model.Cardnews;
import fitpet_be.infrastructure.persistence.JpaCardnewsRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface CardnewsRepository extends JpaCardnewsRepository {

    @Query("SELECT c FROM Cardnews c ORDER BY c.createdAt DESC ")
    Page<Cardnews> findAllByOrderByDesc(Pageable pageable);


    @Query("SELECT c FROM Cardnews c ORDER BY c.createdAt ASC ")
    Page<Cardnews> findAllByOrderByAsc(Pageable pageable);

    @Query("SELECT count(c) FROM Cardnews c")
    Long cardNewsTotalCount();

    @Query("SELECT c FROM Cardnews c WHERE c.title LIKE %:keyword% OR c.content LIKE %:keyword%")
    Page<Cardnews> searchAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
