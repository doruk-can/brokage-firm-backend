package com.demo.brokagefirmbackend.controller;

import com.demo.brokagefirmbackend.model.request.CreateOrderRequest;
import com.demo.brokagefirmbackend.model.response.OrderResponse;
import com.demo.brokagefirmbackend.service.OrderService;
import com.demo.brokagefirmbackend.util.UserUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserUtils userUtils;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        Long customerId = userUtils.getCurrentUserId();
        log.info("Create order request received for customerId: {}, request: {}", customerId, createOrderRequest);
        OrderResponse orderResponse = orderService.createOrder(customerId, createOrderRequest);
        log.info("Order created successfully for customerId: {}, response: {}", customerId, orderResponse);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Long customerId = userUtils.getCurrentUserId();
        log.info("List orders request received for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);
        List<OrderResponse> orderResponseList = orderService.listOrders(customerId, startDate, endDate);
        log.info("Orders listed successfully for customerId: {}, response: {}", customerId, orderResponseList);
        return ResponseEntity.ok(orderResponseList);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        Long customerId = userUtils.getCurrentUserId();
        log.info("Cancel order request received for customerId: {}, orderId: {}", customerId, orderId);
        orderService.cancelOrder(customerId, orderId);
        log.info("Order canceled successfully for customerId: {}, orderId: {}", customerId, orderId);
        return ResponseEntity.noContent().build();
    }
}
