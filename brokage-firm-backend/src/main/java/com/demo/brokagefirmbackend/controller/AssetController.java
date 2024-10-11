package com.demo.brokagefirmbackend.controller;

import com.demo.brokagefirmbackend.model.request.DepositRequest;
import com.demo.brokagefirmbackend.model.request.WithdrawRequest;
import com.demo.brokagefirmbackend.model.response.AssetResponse;
import com.demo.brokagefirmbackend.service.AssetService;
import com.demo.brokagefirmbackend.util.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final UserUtils userUtils;

    @PostMapping("/deposit")
    public ResponseEntity<Void> depositMoney(@Valid @RequestBody DepositRequest depositRequest) {
        Long customerId = userUtils.getCurrentUserId();
        log.info("Deposit request received for customerId: {}, amount: {}", customerId, depositRequest.getAmount());
        assetService.depositMoney(customerId, depositRequest);
        log.info("Deposit successful for customerId: {}", customerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdrawMoney(@Valid@RequestBody WithdrawRequest withdrawRequest) {
        Long customerId = userUtils.getCurrentUserId();
        log.info("Withdraw request received for customerId: {}, amount: {}, iban: {}", customerId, withdrawRequest.getAmount(), withdrawRequest.getIban());
        assetService.withdrawMoney(customerId, withdrawRequest);
        log.info("Withdraw successful for customerId: {}", customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AssetResponse>> listAssets() {
        Long customerId = userUtils.getCurrentUserId();
        log.info("List assets request received for customerId: {}", customerId);
        List<AssetResponse> assets = assetService.listAssets(customerId);
        log.info("List assets successful for customerId: {}, assets: {}", customerId, assets);
        return ResponseEntity.ok(assets);
    }
}