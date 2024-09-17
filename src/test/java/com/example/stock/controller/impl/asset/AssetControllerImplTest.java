package com.example.stock.controller.impl.asset;

import com.example.stock.controller.model.Asset;
import com.example.stock.service.AssetService;
import com.example.stock.service.model.asset.AssetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssetControllerImplTest {

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetControllerImpl assetControllerImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositMoney_Success() {
        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setAssetName("TRY");
        assetDTO.setUsableSize(new BigDecimal("1000"));

        when(assetService.depositMoney(anyLong(), any(BigDecimal.class), anyString()))
                .thenReturn(assetDTO);

        Asset result = assetControllerImpl.depositMoney(1L, new BigDecimal("100"), "TRY");

        assertNotNull(result);
        assertEquals("TRY", result.getAssetName());
        assertEquals(new BigDecimal("1000"), result.getUsableSize());
        verify(assetService, times(1)).depositMoney(anyLong(), any(BigDecimal.class), anyString());
    }

    @Test
    void testWithdrawMoney_Success() {
        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setAssetName("TRY");
        assetDTO.setUsableSize(new BigDecimal("500"));

        when(assetService.withdrawMoney(anyLong(), any(BigDecimal.class), anyString()))
                .thenReturn(assetDTO);

        Asset result = assetControllerImpl.withdrawMoney(1L, new BigDecimal("100"), "TRY");

        assertNotNull(result);
        assertEquals("TRY", result.getAssetName());
        assertEquals(new BigDecimal("500"), result.getUsableSize());
        verify(assetService, times(1)).withdrawMoney(anyLong(), any(BigDecimal.class), anyString());
    }

    @Test
    void testListAssets_Success() {
        AssetDTO assetDTO1 = new AssetDTO();
        assetDTO1.setAssetName("TRY");
        assetDTO1.setUsableSize(new BigDecimal("1000"));

        AssetDTO assetDTO2 = new AssetDTO();
        assetDTO2.setAssetName("BTC");
        assetDTO2.setUsableSize(new BigDecimal("2"));

        List<AssetDTO> assetDTOList = Arrays.asList(assetDTO1, assetDTO2);

        when(assetService.listAssets(anyLong())).thenReturn(assetDTOList);

        List<Asset> result = assetControllerImpl.listAssets(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TRY", result.get(0).getAssetName());
        assertEquals(new BigDecimal("1000"), result.get(0).getUsableSize());
        assertEquals("BTC", result.get(1).getAssetName());
        assertEquals(new BigDecimal("2"), result.get(1).getUsableSize());

        verify(assetService, times(1)).listAssets(anyLong());
    }

    @Test
    void testDepositMoney_Exception() {
        when(assetService.depositMoney(anyLong(), any(BigDecimal.class), anyString()))
                .thenThrow(new RuntimeException("Error while depositing money"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assetControllerImpl.depositMoney(1L, new BigDecimal("100"), "TRY");
        });

        assertEquals("Error while depositing money", exception.getMessage());
        verify(assetService, times(1)).depositMoney(anyLong(), any(BigDecimal.class), anyString());
    }

    @Test
    void testWithdrawMoney_Exception() {
        when(assetService.withdrawMoney(anyLong(), any(BigDecimal.class), anyString()))
                .thenThrow(new RuntimeException("Error while withdrawing money"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assetControllerImpl.withdrawMoney(1L, new BigDecimal("100"), "TRY");
        });

        assertEquals("Error while withdrawing money", exception.getMessage());
        verify(assetService, times(1)).withdrawMoney(anyLong(), any(BigDecimal.class), anyString());
    }
}
