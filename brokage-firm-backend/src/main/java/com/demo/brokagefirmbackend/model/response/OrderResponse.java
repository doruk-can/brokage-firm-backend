package com.demo.brokagefirmbackend.model.response;

import com.demo.brokagefirmbackend.model.enums.OrderSide;
import com.demo.brokagefirmbackend.model.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private Long id;

    private String assetName;

    private OrderSide orderSide;

    private Double size;

    private Double price;

    private OrderStatus status;

    private LocalDateTime createDate;
}
