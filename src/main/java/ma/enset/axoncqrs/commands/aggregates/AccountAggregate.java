package ma.enset.axoncqrs.commands.aggregates;

import lombok.NoArgsConstructor;
import ma.enset.axoncqrs.commonApi.commands.CreateAccountCommand;
import ma.enset.axoncqrs.commonApi.commands.CreditAccountCommand;
import ma.enset.axoncqrs.commonApi.enums.AccountStatus;
import ma.enset.axoncqrs.commonApi.events.AccountActivatedEvent;
import ma.enset.axoncqrs.commonApi.events.AccountCreatedEvent;
import ma.enset.axoncqrs.commonApi.events.AccountCreditedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        // Required by Axon Framework
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command){
        if(command.getBalance() < 0){
            throw new IllegalArgumentException("Initial balance cannot be less than 0");
        }
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(),
                command.getBalance(),
                command.getCurrency(),
                AccountStatus.CREATED
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId = event.getId();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.status = AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                this.accountId,
                AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command){
        if(command.getAmount() <= 0){
            throw new IllegalArgumentException("Credit amount should be greater than 0");
        }
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance += event.getAmount();
    }
}
