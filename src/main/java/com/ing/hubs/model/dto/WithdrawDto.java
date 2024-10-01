package com.ing.hubs.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class WithdrawDto {
    @NonNull
    String customer;
    @NonNull
    @Min(value = 1, message = "Amount must be positive value!")
    Integer amount;
    String IBAN;
}
