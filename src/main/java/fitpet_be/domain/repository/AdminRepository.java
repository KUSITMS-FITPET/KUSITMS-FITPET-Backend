package fitpet_be.domain.repository;

import fitpet_be.domain.model.Admin;
import fitpet_be.infrastructure.persistence.JpaAdminRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface AdminRepository extends JpaAdminRepository {
    @Query("SELECT a FROM Admin a ORDER BY a.createdAt DESC ")
    List<Admin> findAllByOrderByDesc();

}
