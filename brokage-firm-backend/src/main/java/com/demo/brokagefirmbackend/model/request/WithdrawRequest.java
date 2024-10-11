package com.demo.brokagefirmbackend.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class WithdrawRequest {

    @NotNull
    @Positive
    private Double amount;

    @NotBlank
    private String iban;
}
