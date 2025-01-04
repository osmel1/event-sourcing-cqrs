package ma.enset.axoncqrs.query.repositories;

import ma.enset.axoncqrs.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
