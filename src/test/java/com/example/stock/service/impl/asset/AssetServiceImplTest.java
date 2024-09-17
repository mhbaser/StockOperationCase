package com.example.stock.service.impl.asset;

import com.example.stock.data.AssetRepository;
import com.example.stock.data.model.asset.AssetEntity;
import com.example.stock.service.CustomerService;
import com.example.stock.service.mapper.AssetServiceMapper;
import com.example.stock.service.model.asset.AssetDTO;
import com.example.stock.service.model.customer.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AssetServiceImpl assetServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositMoney_NewAsset_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setAssetName("TRY");
        assetDTO.setUsableSize(BigDecimal.ZERO);
        assetDTO.setSize(BigDecimal.ZERO);

        when(customerService.findCustomerById(anyLong())).thenReturn(customerDTO);
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), anyString())).thenReturn(Optional.empty());
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO));

        AssetDTO result = assetServiceImpl.depositMoney(1L, BigDecimal.valueOf(100), "TRY");

        assertNotNull(result);
        assertEquals("TRY", result.getAssetName());
        assertEquals(BigDecimal.valueOf(0), result.getUsableSize());
        assertEquals(BigDecimal.valueOf(0), result.getSize());
        verify(assetRepository, times(1)).save(any(AssetEntity.class));
    }

    @Test
    void testDepositMoney_ExistingAsset_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setAssetName("TRY");
        assetDTO.setUsableSize(BigDecimal.valueOf(200));
        assetDTO.setSize(BigDecimal.valueOf(200));

        when(customerService.findCustomerById(anyLong())).thenReturn(customerDTO);
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), anyString()))
                .thenReturn(Optional.of(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO)));
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO));

        AssetDTO result = assetServiceImpl.depositMoney(1L, BigDecimal.valueOf(100), "TRY");

        assertNotNull(result);
        assertEquals("TRY", result.getAssetName());
        assertEquals(BigDecimal.valueOf(200), result.getUsableSize());
        assertEquals(BigDecimal.valueOf(200), result.getSize());
        verify(assetRepository, times(1)).save(any(AssetEntity.class));
    }

    @Test
    void testWithdrawMoney_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setAssetName("TRY");
        assetDTO.setUsableSize(BigDecimal.valueOf(200));
        assetDTO.setSize(BigDecimal.valueOf(200));

        when(customerService.findCustomerById(anyLong())).thenReturn(customerDTO);
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), anyString()))
                .thenReturn(Optional.of(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO)));
        when(assetRepository.save(any(AssetEntity.class))).thenReturn(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO));

        AssetDTO result = assetServiceImpl.withdrawMoney(1L, BigDecimal.valueOf(100), "TRY");

        assertNotNull(result);
        assertEquals("TRY", result.getAssetName());
        assertEquals(BigDecimal.valueOf(200), result.getUsableSize());
        assertEquals(BigDecimal.valueOf(200), result.getSize());
        verify(assetRepository, times(1)).save(any(AssetEntity.class));
    }

    @Test
    void testWithdrawMoney_InsufficientBalance() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(1L);

        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setAssetName("TRY");
        assetDTO.setUsableSize(BigDecimal.valueOf(50));

        when(customerService.findCustomerById(anyLong())).thenReturn(customerDTO);
        when(assetRepository.findByCustomerIdAndAssetName(anyLong(), anyString()))
                .thenReturn(Optional.of(AssetServiceMapper.INSTANCE.toAssetEntity(assetDTO)));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assetServiceImpl.withdrawMoney(1L, BigDecimal.valueOf(100), "TRY");
        });

        assertEquals("Insufficient balance to perform withdraw.", exception.getMessage());
        verify(assetRepository, never()).save(any(AssetEntity.class));
    }

    @Test
    void testListAssets_Success() {
        AssetEntity assetEntity1 = new AssetEntity();
        assetEntity1.setAssetName("TRY");
        assetEntity1.setUsableSize(BigDecimal.valueOf(100));

        AssetEntity assetEntity2 = new AssetEntity();
        assetEntity2.setAssetName("BTC");
        assetEntity2.setUsableSize(BigDecimal.valueOf(0.5));

        when(assetRepository.findByCustomerId(anyLong())).thenReturn(Arrays.asList(assetEntity1, assetEntity2));

        List<AssetDTO> result = assetServiceImpl.listAssets(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TRY", result.get(0).getAssetName());
        assertEquals(BigDecimal.valueOf(100), result.get(0).getUsableSize());
        assertEquals("BTC", result.get(1).getAssetName());
        assertEquals(BigDecimal.valueOf(0.5), result.get(1).getUsableSize());
        verify(assetRepository, times(1)).findByCustomerId(anyLong());
    }

    @Test
    void testCheckInputs_InvalidAmount() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assetServiceImpl.checkInputs(BigDecimal.valueOf(-100));
        });

        assertEquals("Please enter a valid positive value.", exception.getMessage());
    }
}
