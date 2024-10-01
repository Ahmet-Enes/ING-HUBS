package com.ing.hubs.service;

import com.ing.hubs.model.dto.DepositDto;
import com.ing.hubs.model.dto.WithdrawDto;
import com.ing.hubs.model.entity.Asset;
import com.ing.hubs.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository repository;

    public Asset depositMoney(DepositDto dto) {
        Asset asset = repository.findByCustomerIdAndAssetName(dto.getCustomer(), "TRY")
                .orElse(new Asset());
        asset.setCustomerId(dto.getCustomer());
        asset.setAssetName("TRY");
        asset.setSize(asset.getSize() + dto.getAmount());
        asset.setUsableSize(asset.getUsableSize() + dto.getAmount());
        return repository.save(asset);
    }

    public Asset withdrawMoney(WithdrawDto dto) {
        Asset asset = repository.findByCustomerIdAndAssetName(dto.getCustomer(), "TRY")
                .orElseThrow(() -> new RuntimeException("Customer not found with TRY asset: " + dto.getCustomer()));
        if (asset.getUsableSize() < dto.getAmount()) {
            throw new RuntimeException("Insufficient funds");
        }
        asset.setSize(asset.getSize() - dto.getAmount());
        asset.setUsableSize(asset.getUsableSize() - dto.getAmount());
        return repository.save(asset);
    }

    public List<Asset> getAssets(String customerId) {
        return repository.findByCustomerId(customerId);
    }
}
