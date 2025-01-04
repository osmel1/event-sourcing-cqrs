package ma.enset.axoncqrs.commonApi.commands

import ma.enset.axoncqrs.commonApi.enums.AccountStatus
import org.axonframework.modelling.command.TargetAggregateIdentifier


abstract class BaseCommand<T>(
    @TargetAggregateIdentifier
    open val id: T
)

data class CreateAccountCommand(
    override val id: String,
    val balance: Double,
    val currency: String,
    val status: AccountStatus
) : BaseCommand<String>(id)

data class UpdateAccountCommand(
    override val id: String,
    val balance: Double
) : BaseCommand<String>(id)

data class CreditAccountCommand(
    override val id: String,
    val amount: Double,
    val currency: String
) : BaseCommand<String>(id)

