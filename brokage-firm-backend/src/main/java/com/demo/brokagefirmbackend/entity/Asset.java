package com.demo.brokagefirmbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "assets")
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String assetName;

    private Double size;

    private Double usableSize;

    public Asset(Long customerId, String assetName) {
        this.customerId = customerId;
        this.assetName = assetName;
        this.size = 0.0;
        this.usableSize = 0.0;
    }
}