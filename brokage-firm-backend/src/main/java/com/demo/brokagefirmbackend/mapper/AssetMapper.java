package com.demo.brokagefirmbackend.mapper;

import com.demo.brokagefirmbackend.entity.Asset;
import com.demo.brokagefirmbackend.model.response.AssetResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    AssetResponse toDto(Asset entity);

    List<AssetResponse> toDtoList(List<Asset> entities);
}