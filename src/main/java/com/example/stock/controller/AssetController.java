package com.example.stock.controller;

import com.example.stock.controller.model.Asset;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/asset")
public interface AssetController {

    @PostMapping("/deposit")
    Asset depositMoney(@RequestParam Long customerId, @RequestParam BigDecimal amount,@RequestParam String assetName);

    @PostMapping("/withdraw")
    Asset withdrawMoney(@RequestParam Long customerId, @RequestParam BigDecimal amount,@RequestParam String assetName);

    @GetMapping("/list")
    List<Asset> listAssets(@RequestParam Long customerId);


}
