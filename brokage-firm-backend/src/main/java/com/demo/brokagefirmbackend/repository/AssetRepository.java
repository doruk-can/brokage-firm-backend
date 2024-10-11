package com.demo.brokagefirmbackend.repository;

import com.demo.brokagefirmbackend.entity.Asset;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Asset a WHERE a.customerId = :customerId AND a.assetName = :assetName")
    Optional<Asset> findByCustomerIdAndAssetNameForUpdate(@Param("customerId") Long customerId, @Param("assetName") String assetName);

    List<Asset> findAllByCustomerId(Long customerId);


}