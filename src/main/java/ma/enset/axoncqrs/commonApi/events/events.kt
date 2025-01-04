package ma.enset.axoncqrs.commonApi.events

import ma.enset.axoncqrs.commonApi.enums.AccountStatus


abstract class BaseEvent<T>(
    open val id: T
)

data class AccountCreatedEvent(
    override val id: String,
    val balance: Double,
    val currency: String,
    val status: AccountStatus
) : BaseEvent<String>(id)

data class UpdateAccountCommand(
    override val id: String,
    val balance: Double
) : BaseEvent<String>(id)

data class AccountActivatedEvent(
    override val id: String,
    val status : AccountStatus
) : BaseEvent<String>(id)

data class AccountCreditedEvent(
    override val id: String,
    val amount: Double,
    val currency: String
) : BaseEvent<String>(id)

