package com.demo.brokagefirmbackend.service;

import com.demo.brokagefirmbackend.entity.Asset;
import com.demo.brokagefirmbackend.entity.Order;
import com.demo.brokagefirmbackend.exception.InsufficientAssetException;
import com.demo.brokagefirmbackend.exception.InsufficientFundsException;
import com.demo.brokagefirmbackend.exception.InvalidOrderStatusException;
import com.demo.brokagefirmbackend.exception.OrderNotFoundException;
import com.demo.brokagefirmbackend.mapper.OrderMapper;
import com.demo.brokagefirmbackend.model.enums.OrderSide;
import com.demo.brokagefirmbackend.model.enums.OrderStatus;
import com.demo.brokagefirmbackend.model.request.CreateOrderRequest;
import com.demo.brokagefirmbackend.model.response.OrderResponse;
import com.demo.brokagefirmbackend.repository.AssetRepository;
import com.demo.brokagefirmbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final AssetRepository assetRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(Long customerId, CreateOrderRequest createOrderRequest) {
        Order order = orderMapper.toEntity(createOrderRequest, customerId);

        final double requiredAmount = order.getPrice() * order.getSize();

        if (OrderSide.BUY.equals(order.getOrderSide())) {
            handleBuyOrder(customerId, requiredAmount);
        } else if (OrderSide.SELL.equals(order.getOrderSide())) {
            handleSellOrder(customerId, order);
        }

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    public List<OrderResponse> listOrders(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orderList = orderRepository.findAllByCustomerIdAndCreateDateBetween(customerId, startDate, endDate)
                .orElseThrow(() -> new OrderNotFoundException("No orders found."));
        return orderMapper.toDtoList(orderList);
    }

    @Transactional
    public void cancelOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        if (!order.getCustomerId().equals(customerId)) {
            throw new AccessDeniedException("You don't have permission to cancel this order.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("Only pending orders can be canceled.");
        }

        if (OrderSide.BUY.equals(order.getOrderSide())) {
            refundBuyOrder(customerId, order);
        } else if (OrderSide.SELL.equals(order.getOrderSide())) {
            refundSellOrder(customerId, order);
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    private void refundBuyOrder(Long customerId, Order order) {
        Asset asset = assetRepository.findByCustomerIdAndAssetNameForUpdate(customerId, "TRY")
                .orElseThrow(() -> new RuntimeException("TRY asset not found for customer."));

        double refundAmount = order.getPrice() * order.getSize();

        asset.setUsableSize(asset.getUsableSize() + refundAmount);
        assetRepository.save(asset);
    }

    private void refundSellOrder(Long customerId, Order order) {
        Asset assetToSell = assetRepository.findByCustomerIdAndAssetNameForUpdate(customerId, order.getAssetName())
                .orElseThrow(() -> new RuntimeException("Asset not found for customer."));

        assetToSell.setUsableSize(assetToSell.getUsableSize() + order.getSize());
        assetRepository.save(assetToSell);
    }

    private void handleBuyOrder(Long customerId, double requiredAmount) {
        Asset asset = assetRepository.findByCustomerIdAndAssetNameForUpdate(customerId, "TRY")
                .orElseThrow(() -> new InsufficientFundsException("TRY asset not found for customer."));

        if (asset.getUsableSize() < requiredAmount) {
            throw new InsufficientFundsException("Not enough TRY balance to place BUY order.");
        }

        asset.setUsableSize(asset.getUsableSize() - requiredAmount);
        assetRepository.save(asset);
    }

    private void handleSellOrder(Long customerId, Order order) {
        Asset assetToSell = assetRepository.findByCustomerIdAndAssetNameForUpdate(customerId, order.getAssetName())
                .orElseThrow(() -> new InsufficientAssetException("Asset not found for customer."));

        if (assetToSell.getUsableSize() < order.getSize()) {
            throw new InsufficientAssetException("Not enough " + order.getAssetName() + " balance to place SELL order.");
        }

        assetToSell.setUsableSize(assetToSell.getUsableSize() - order.getSize());
        assetRepository.save(assetToSell);
    }

}
