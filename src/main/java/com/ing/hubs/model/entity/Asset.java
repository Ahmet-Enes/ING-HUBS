package com.ing.hubs.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"customerId", "assetName"}))
public class Asset {
    @Id
    @NonNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NonNull
    private String customerId;
    @NonNull
    private String assetName;
    @NonNull
    private int size;
    @NonNull
    private int usableSize;
}
