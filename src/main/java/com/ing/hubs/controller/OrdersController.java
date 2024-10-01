package com.ing.hubs.controller;

import com.ing.hubs.model.dto.CreateOrderDto;
import com.ing.hubs.model.dto.OrderFilter;
import com.ing.hubs.model.entity.Orders;
import com.ing.hubs.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrdersController {


    private final OrdersService service;

    @PreAuthorize("hasRole('ADMIN') or #dto.customer == principal.username")
    @PostMapping
    public Orders createOrder(@RequestBody @Validated CreateOrderDto dto) {
        return service.createOrder(dto);
    };

    @PreAuthorize("hasRole('ADMIN') or #filter.customerId == principal.username")
    @PostMapping("/list")
    public List<Orders> getOrders(@RequestBody OrderFilter filter) {
        return service.getOrders(filter);
    }

    @DeleteMapping("/{id}")
    public Orders deleteOrder(@PathVariable Integer id) {
        return service.deleteOrder(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Orders matchOrder(@PathVariable Integer id) {
        return service.matchOrder(id);
    }
}
