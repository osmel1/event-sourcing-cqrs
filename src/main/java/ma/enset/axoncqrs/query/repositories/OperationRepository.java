package ma.enset.axoncqrs.query.repositories;

import ma.enset.axoncqrs.query.entities.Account;
import ma.enset.axoncqrs.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
