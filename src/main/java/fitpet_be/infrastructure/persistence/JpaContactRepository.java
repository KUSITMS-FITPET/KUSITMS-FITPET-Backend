package fitpet_be.infrastructure.persistence;

import fitpet_be.domain.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaContactRepository extends JpaRepository<Contact, Long> {

}
