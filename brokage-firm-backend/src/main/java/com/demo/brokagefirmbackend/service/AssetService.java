package com.demo.brokagefirmbackend.service;

import com.demo.brokagefirmbackend.entity.Asset;
import com.demo.brokagefirmbackend.exception.InsufficientFundsException;
import com.demo.brokagefirmbackend.exception.WithdrawalProcessingException;
import com.demo.brokagefirmbackend.mapper.AssetMapper;
import com.demo.brokagefirmbackend.model.request.DepositRequest;
import com.demo.brokagefirmbackend.model.request.WithdrawRequest;
import com.demo.brokagefirmbackend.model.response.AssetResponse;
import com.demo.brokagefirmbackend.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetService {


    private final AssetRepository assetRepository;
    private final AssetMapper assetMapper;

    @Transactional
    public void depositMoney(Long customerId, DepositRequest depositRequest) {
        Double amount = depositRequest.getAmount();

        Asset asset = assetRepository.findByCustomerIdAndAssetNameForUpdate(customerId, "TRY")
                .orElse(null);

        if (asset == null) {
            asset = new Asset(customerId, "TRY");
        }

        asset.setSize(asset.getSize() + amount);
        asset.setUsableSize(asset.getUsableSize() + amount);

        assetRepository.save(asset);
    }

    @Transactional
    public void withdrawMoney(Long customerId, WithdrawRequest withdrawRequest) {
        Double amount = withdrawRequest.getAmount();
        String iban = withdrawRequest.getIban();

        Asset tryAsset = assetRepository.findByCustomerIdAndAssetNameForUpdate(customerId, "TRY")
                .orElseThrow(() -> new InsufficientFundsException("TRY asset not found for customer."));

        if (tryAsset.getUsableSize() < amount) {
            throw new InsufficientFundsException("Not enough TRY balance.");
        }

        tryAsset.setSize(tryAsset.getSize() - amount);
        tryAsset.setUsableSize(tryAsset.getUsableSize() - amount);

        assetRepository.save(tryAsset);

        boolean withdrawalSuccess = processWithdrawal(iban, amount);

        if (!withdrawalSuccess) {
            throw new WithdrawalProcessingException("Failed to process withdrawal to IBAN: " + iban);
        }
    }

    private boolean processWithdrawal(String iban, Double amount) {
        return true; // Simulating successful withdrawal from the bank
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> listAssets(Long customerId) {
        List<Asset> assets = assetRepository.findAllByCustomerId(customerId);
        return assetMapper.toDtoList(assets);
    }
}