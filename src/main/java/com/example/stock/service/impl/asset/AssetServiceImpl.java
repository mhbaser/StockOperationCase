package com.example.stock.service.impl.asset;

import com.example.stock.data.AssetRepository;
import com.example.stock.data.model.asset.AssetEntity;
import com.example.stock.service.AssetService;
import com.example.stock.service.CustomerService;
import com.example.stock.service.mapper.AssetServiceMapper;
import com.example.stock.service.model.asset.AssetDTO;
import com.example.stock.service.model.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AssetServiceImpl implements AssetService {

    AssetRepository assetRepository;
    CustomerService customerService;

    @Autowired
    AssetServiceImpl(AssetRepository assetRepository, CustomerService customerService) {
        this.assetRepository = assetRepository;
        this.customerService = customerService;
    }

    @Override
    public AssetDTO depositMoney(Long customerId, BigDecimal amount, String assetName) {

        checkCustomerExist(customerId);
        checkInputs(amount);
        AssetDTO assetDTO = checkAssetExist(customerId, assetName);

        if (Objects.isNull(assetDTO.getId())) {
            assetDTO.setCustomerId(customerId);
            assetDTO.setAssetName(assetName);
            assetDTO.setSize(amount);
            assetDTO.setUsableSize(amount);
        }else {
            assetDTO.setUsableSize(assetDTO.getUsableSize().add(amount));
            assetDTO.setSize(assetDTO.getSize().add(amount));
        }

        try {
            return AssetServiceMapper.INSTANCE.toAssetDTO(assetRepository.save(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Getting an error while deposit assets.");
        }
    }

    @Override
    public AssetDTO withdrawMoney(Long customerId, BigDecimal amount, String assetName) {

        checkCustomerExist(customerId);
        checkInputs(amount);
        AssetDTO assetDTO = checkAssetExist(customerId, assetName);
        if (Objects.isNull(assetDTO)) {
            throw new RuntimeException("First you should deposit an asset to withdraw.");
        } else if (assetDTO.getUsableSize().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance to perform withdraw.");
        } else if (assetDTO.getUsableSize().compareTo(amount) >= 0) {
            assetDTO.setUsableSize(assetDTO.getUsableSize().subtract(amount));
            assetDTO.setSize(assetDTO.getSize().subtract(amount));
        }

        return AssetServiceMapper.INSTANCE.toAssetDTO(assetRepository.save(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO)));
    }

    @Override
    public List<AssetDTO> listAssets(Long customerId) {
        List<AssetEntity> assetEntityList = assetRepository.findByCustomerId(customerId);
        List<AssetDTO> assetDTOList = new ArrayList<>();
        for (AssetEntity assetEntity : assetEntityList) {
            AssetDTO assetDTO = AssetServiceMapper.INSTANCE.toAssetDTO(assetEntity);
            assetDTOList.add(assetDTO);
        }

        return assetDTOList;
    }


    public void checkCustomerExist(Long customerId) {

        CustomerDTO customerDTO = customerService.findCustomerById(customerId);
        if (Objects.isNull(customerDTO)) {
            throw new RuntimeException("First you should create a customer.");
        }
    }

    public AssetDTO checkAssetExist(Long customerId, String assetName) {

        AssetEntity assetDTO = assetRepository.findByCustomerIdAndAssetName(customerId, assetName).orElse(new AssetEntity());
        return AssetServiceMapper.INSTANCE.toAssetDTO(assetDTO);
    }

    public void checkInputs(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Please enter a valid positive value.");
        }
    }

}
