package com.demo.brokagefirmbackend.model.response;

import lombok.Data;

@Data
public class AssetResponse {

    private Long id;
    private String assetName;
    private Double size;
    private Double usableSize;
}