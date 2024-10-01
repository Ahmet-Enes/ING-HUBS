package com.ing.hubs.model.dto;

import com.ing.hubs.model.entity.Orders;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class CreateOrderDto {
    @NonNull
    private String customer;
    @NonNull
    private String asset;
    @NonNull
    private Side side;
    @NonNull
    @Min(value = 1, message = "Size must be positive value!")
    private int size;
    @NonNull
    @Min(value = 1, message = "Price must be positive value!")
    private int price;

    public static void mapToEntity(CreateOrderDto dto, Orders orders) {
        orders.setCustomerId(dto.getCustomer());
        orders.setAssetName(dto.getAsset());
        orders.setOrderSide(dto.getSide());
        orders.setSize(dto.getSize());
        orders.setPrice(dto.getPrice());
    }
}