package com.example.stock.data;

import com.example.stock.data.model.asset.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity,Long> {

    AssetEntity save(AssetEntity assetEntity);
    Optional<AssetEntity> findByCustomerIdAndAssetName(Long customerId,String assetName);
    List<AssetEntity> findByCustomerId(Long customerId);
}
