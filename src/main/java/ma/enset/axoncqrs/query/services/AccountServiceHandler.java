package ma.enset.axoncqrs.query.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.axoncqrs.commonApi.enums.OperationType;
import ma.enset.axoncqrs.commonApi.events.AccountActivatedEvent;
import ma.enset.axoncqrs.commonApi.events.AccountCreatedEvent;
import ma.enset.axoncqrs.commonApi.events.AccountCreditedEvent;
import ma.enset.axoncqrs.commonApi.queries.GetAllAccountsQuery;
import ma.enset.axoncqrs.query.entities.Account;
import ma.enset.axoncqrs.query.entities.Operation;
import ma.enset.axoncqrs.query.repositories.AccountRepository;
import ma.enset.axoncqrs.query.repositories.OperationRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AccountServiceHandler {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("**********************");
        log.info("AccountCreatedEvent received");

        Account account = new Account();
        account.setId(event.getId());
        account.setBalance(event.getBalance());
        account.setCurrency(event.getCurrency());
        account.setStatus(event.getStatus());

        accountRepository.save(account);
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("**********************");
        log.info("AccountCreditedEvent received");

        Account account = accountRepository.findById(event.getId()).get();
        Operation operation = new Operation();
        operation.setAmount(event.getAmount());
        operation.setAccount(account);
        operation.setType(OperationType.CREDIT);
        operation.setCurrency(account.getCurrency());
        operation.setOperationDate(Date.from(Instant.now()));
        operationRepository.save(operation);

        account.setBalance(account.getBalance() + event.getAmount());
        accountRepository.save(account);
    }

    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }
}
