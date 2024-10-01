package com.ing.hubs.controller;

import com.ing.hubs.model.dto.DepositDto;
import com.ing.hubs.model.dto.WithdrawDto;
import com.ing.hubs.model.entity.Asset;
import com.ing.hubs.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService service;

    @PreAuthorize("hasRole('ADMIN') or #dto.customer == principal.username")
    @PostMapping("/deposit")
    public Asset depositMoney(@RequestBody @Validated DepositDto dto) {
        return service.depositMoney(dto);
    }

    @PreAuthorize("hasRole('ADMIN') or #dto.customer == principal.username")
    @PostMapping("/withdraw")
    public Asset withdrawMoney(@RequestBody @Validated WithdrawDto dto) {
        return service.withdrawMoney(dto);
    }

    @PreAuthorize("hasRole('ADMIN') or #customerId == principal.username")
    @GetMapping("/{customerId}")
    public List<Asset> getAssets(@PathVariable String customerId) {
        return service.getAssets(customerId);
    }
}
