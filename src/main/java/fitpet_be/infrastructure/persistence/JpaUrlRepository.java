package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUrlRepository extends JpaRepository<Url, Long> {

}
