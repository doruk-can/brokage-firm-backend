package com.demo.brokagefirmbackend.entity;

import com.demo.brokagefirmbackend.model.enums.OrderSide;
import com.demo.brokagefirmbackend.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String assetName;

    @Enumerated(EnumType.STRING)
    private OrderSide orderSide;

    private Double size;

    private Double price;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime createDate;
}