package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAdminRepository extends JpaRepository<Admin, String> {

}
