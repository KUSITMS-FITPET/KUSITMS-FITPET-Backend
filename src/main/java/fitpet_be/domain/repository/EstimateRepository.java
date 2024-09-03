package fitpet_be.domain.repository;

import fitpet_be.application.dto.request.EstimateSearchRequest;
import fitpet_be.domain.model.Estimate;
import fitpet_be.infrastructure.persistence.JpaEstimateRepository;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstimateRepository extends JpaEstimateRepository {

    @Query("SELECT e FROM Estimate e ORDER BY e.createdAt DESC ")
    Page<Estimate> findAllByOrderByDesc(Pageable pageable);


    @Query("SELECT e FROM Estimate e ORDER By e.createdAt ASC")
    Page<Estimate> findAllByOrderByAsc(Pageable pageable);

    @Query("SELECT count(e) FROM Estimate e")
    Long estimateTotalCount();

    @Query("SELECT e FROM Estimate e WHERE "
            + "(e.createdAt >= :startDate AND e.createdAt <= :endDate) AND "
            + "(e.refeere = :refeere) AND "
            + "(e.petInfo = :petInfo) AND "
            + "(e.phoneNumber LIKE %:phoneNumber%)"
            + "ORDER BY e.createdAt DESC ")
    Page<Estimate> findAllBySearch(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate,
                                   @Param("refeere") String refeere, @Param("petInfo") String petInfo,
                                   @Param("phoneNumber") String phoneNumber, Pageable pageable);

}
