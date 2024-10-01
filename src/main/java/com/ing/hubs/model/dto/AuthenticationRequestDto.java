package com.ing.hubs.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class AuthenticationRequestDto {
    @NonNull
    private String username;
    @NonNull
    private String password;
}
