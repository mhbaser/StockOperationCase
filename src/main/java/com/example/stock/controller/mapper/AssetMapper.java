package com.example.stock.controller.mapper;

import com.example.stock.controller.model.Asset;
import com.example.stock.service.model.asset.AssetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssetMapper {

    AssetMapper INSTANCE = Mappers.getMapper(AssetMapper.class);

    Asset toAsset(AssetDTO assetDTO);
    AssetDTO toAssetDTO(Asset asset);
}
