package com.ing.hubs.model.entity;

import com.ing.hubs.model.dto.Side;
import com.ing.hubs.model.dto.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Orders {
    @Id
    @NonNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String customerId;
    @NonNull
    private String assetName;
    @NonNull
    private Side orderSide;
    @NonNull
    private int size;
    @NonNull
    private int price;
    @NonNull
    private Status status;
    @NonNull
    private LocalDateTime createDate;
}
