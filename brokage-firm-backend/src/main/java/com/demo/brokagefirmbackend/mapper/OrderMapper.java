package com.demo.brokagefirmbackend.mapper;

import com.demo.brokagefirmbackend.entity.Order;
import com.demo.brokagefirmbackend.model.enums.OrderStatus;
import com.demo.brokagefirmbackend.model.request.CreateOrderRequest;
import com.demo.brokagefirmbackend.model.response.OrderResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    Order toEntity(CreateOrderRequest dto,  Long customerId);

    OrderResponse toDto(Order entity);

    List<OrderResponse> toDtoList(List<Order> entities);

    @AfterMapping
    default void setAdditionalFields(@MappingTarget Order order) {
        order.setStatus(OrderStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());
    }
}