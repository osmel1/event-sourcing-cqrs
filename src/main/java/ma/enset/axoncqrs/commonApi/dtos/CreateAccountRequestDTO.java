package ma.enset.axoncqrs.commonApi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.axoncqrs.commonApi.enums.AccountStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDTO {
    private double iniatialBalance;
    private String currency;
    private AccountStatus status;
}
