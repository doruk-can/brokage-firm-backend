package com.demo.brokagefirmbackend.repository;

import com.demo.brokagefirmbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findAllByCustomerIdAndCreateDateBetween(Long customerId, LocalDateTime startDate, LocalDateTime endDate);

}