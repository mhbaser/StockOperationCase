package com.example.stock.service.mapper;

import com.example.stock.data.model.asset.AssetEntity;
import com.example.stock.service.model.asset.AssetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssetServiceMapper {

    AssetServiceMapper INSTANCE = Mappers.getMapper(AssetServiceMapper.class);

    AssetEntity toAssetEntity(AssetDTO assetDTO);
    AssetDTO toAssetDTO(AssetEntity assetEntity);


}
