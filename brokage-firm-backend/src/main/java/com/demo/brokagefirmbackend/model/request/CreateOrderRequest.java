package com.demo.brokagefirmbackend.model.request;

import com.demo.brokagefirmbackend.model.enums.OrderSide;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderRequest {

    @NotEmpty
    private String assetName;
    @NotNull
    private OrderSide orderSide;
    @NotNull
    @Positive
    private Double size;
    @NotNull
    @Positive
    private Double price;
}