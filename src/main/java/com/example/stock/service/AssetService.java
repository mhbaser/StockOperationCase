package com.example.stock.service;

import com.example.stock.service.model.asset.AssetDTO;


import java.math.BigDecimal;
import java.util.List;

public interface AssetService {

     AssetDTO depositMoney(Long customerId, BigDecimal amount,String assetName);

     AssetDTO withdrawMoney(Long customerId, BigDecimal amount,String assetName);

     List<AssetDTO> listAssets(Long customerId);

}
