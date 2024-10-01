package com.ing.hubs.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFilter {
    private String customerId;
    private String asset;
    private LocalDateTime beforeDate;
    private LocalDateTime afterDate;
}
