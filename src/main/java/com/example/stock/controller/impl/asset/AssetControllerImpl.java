package com.example.stock.controller.impl.asset;

import com.example.stock.controller.AssetController;
import com.example.stock.controller.mapper.AssetMapper;
import com.example.stock.controller.model.Asset;
import com.example.stock.service.AssetService;
import com.example.stock.service.model.asset.AssetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AssetControllerImpl implements AssetController {

    private final AssetService assetService;

    @Autowired
    public AssetControllerImpl(AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    public Asset depositMoney(Long customerId, BigDecimal amount, String assetName) {
        try {
            return AssetMapper.INSTANCE.toAsset(assetService.depositMoney(customerId, amount, assetName));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Asset withdrawMoney(Long customerId, BigDecimal amount, String assetName) {
        try {
            return AssetMapper.INSTANCE.toAsset(assetService.withdrawMoney(customerId, amount, assetName));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Asset> listAssets(Long customerId) {
        try{
            List<AssetDTO> assetDTOList = assetService.listAssets(customerId);
            List<Asset> assets = new ArrayList<>();
            for (AssetDTO assetDTO : assetDTOList) {
                Asset asset = AssetMapper.INSTANCE.toAsset(assetDTO);
                assets.add(asset);
            }

            return assets;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
